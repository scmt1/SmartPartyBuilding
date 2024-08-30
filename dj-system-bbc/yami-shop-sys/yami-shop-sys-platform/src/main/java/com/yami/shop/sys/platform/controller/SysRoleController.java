/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.sys.platform.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.sys.common.model.SysRole;
import com.yami.shop.sys.common.service.SysMenuService;
import com.yami.shop.sys.common.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * 角色管理
 * @author lgh
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("/sys/role")
public class SysRoleController{
    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private SysMenuService sysMenuService;

    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiImplicitParam(name = "roleName", value = "角色名称")
    @PreAuthorize("@pms.hasPermission('sys:role:page')")
    public ServerResponseEntity<IPage<SysRole>> page(String roleName, PageParam<SysRole> page){
        IPage<SysRole> sysRoles = sysRoleService.page(page,new LambdaQueryWrapper<SysRole>()
                .like(StrUtil.isNotBlank(roleName),SysRole::getRoleName,roleName)
                .orderByDesc(SysRole::getCreateTime));
        return ServerResponseEntity.success(sysRoles);
    }

    @GetMapping("/list")
    @ApiOperation(value = "角色列表", notes = "角色列表")
    @PreAuthorize("@pms.hasPermission('sys:role:list')")
    public ServerResponseEntity<List<SysRole>> list(){
        List<SysRole> list = sysRoleService.list();
        return ServerResponseEntity.success(list);
    }

    @GetMapping("/info/{roleId}")
    @ApiOperation(value = "角色信息", notes = "角色信息")
    @ApiImplicitParam(name = "roleId", value = "角色id")
    @PreAuthorize("@pms.hasPermission('sys:role:info')")
    public ServerResponseEntity<SysRole> info(@PathVariable("roleId") Long roleId){
        SysRole role = sysRoleService.getById(roleId);
        //查询角色对应的菜单
        List<Long> menuList = sysMenuService.listMenuIdByRoleId(roleId);
        role.setMenuIdList(menuList);

        return ServerResponseEntity.success(role);
    }

    @SysLog("保存角色")
    @PostMapping
    @ApiOperation(value = "保存角色", notes = "保存角色")
    @PreAuthorize("@pms.hasPermission('sys:role:save')")
    public ServerResponseEntity<Void> save(@RequestBody SysRole role){

        if (sysRoleService.count(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleName, role.getRoleName())) > 0) {
            // 系统已存在相同角色名称
            throw new YamiShopBindException("yami.sys.role.identical.name");
        }

        sysRoleService.saveRoleAndRoleMenu(role);
        return ServerResponseEntity.success();
    }

    @SysLog("修改角色")
    @PutMapping
    @ApiOperation(value = "修改角色", notes = "修改角色")
    @PreAuthorize("@pms.hasPermission('sys:role:update')")
    public ServerResponseEntity<Void> update(@RequestBody SysRole role){

        int size = sysRoleService.count(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getRoleName, role.getRoleName())
                .ne(SysRole::getRoleId, role.getRoleId())
        );

        if (size > 0) {
            // 系统已存在相同角色名称
            throw new YamiShopBindException("yami.sys.role.identical.name");
        }

        sysRoleService.updateRoleAndRoleMenu(role);
        return ServerResponseEntity.success();
    }

    @SysLog("删除角色")
    @DeleteMapping
    @ApiOperation(value = "删除角色", notes = "删除角色")
    @ApiImplicitParam(name = "roleIds", value = "角色id")
    @PreAuthorize("@pms.hasPermission('sys:role:delete')")
    public ServerResponseEntity<String> delete(@RequestBody Long[] roleIds){
        List<Long> ids = CollUtil.newArrayList(roleIds);
        sysRoleService.deleteBatch(ids);
        return ServerResponseEntity.success();
    }
}
