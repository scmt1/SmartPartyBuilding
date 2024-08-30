/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.sys.multishop.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.sys.common.model.ShopRole;
import com.yami.shop.sys.common.service.ShopMenuService;
import com.yami.shop.sys.common.service.ShopRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


/**
 * @author yami
 * @date 2021-03-01 17:42:09
 */
@RestController
@RequestMapping("/sys/shopRole" )
@Api(tags = "权限角色接口")
public class ShopRoleController {

    @Autowired
    private ShopRoleService shopRoleService;

    @Autowired
    private ShopMenuService shopMenuService;

    /**
     * 分页查询
     * @param page 分页对象
     * @param shopRole 角色
     * @return 分页数据
     */
    @GetMapping("/page" )
    @PreAuthorize("@pms.hasPermission('sys:shopRole:page')")
    @ApiOperation(value = "分页获取角色", notes = "分页获取角色")
    public ServerResponseEntity<IPage<ShopRole>> getShopRolePage(PageParam<ShopRole> page, ShopRole shopRole) {
        IPage<ShopRole> shopRoles = shopRoleService.page(page,new LambdaQueryWrapper<ShopRole>()
                .eq(ShopRole::getShopId,SecurityUtils.getShopUser().getShopId())
                .like(StrUtil.isNotBlank(shopRole.getRoleName()),ShopRole::getRoleName,shopRole.getRoleName())
                .orderByDesc(ShopRole::getCreateTime));
        return ServerResponseEntity.success(shopRoles);
    }

    /**
     * 角色列表
     */
    @GetMapping("/list")
    @PreAuthorize("@pms.hasPermission('sys:shopRole:list')")
    @ApiOperation(value = "获取角色列表", notes = "获取角色列表")
    public ServerResponseEntity<List<ShopRole>> list(){
        List<ShopRole> list = shopRoleService.list(new LambdaQueryWrapper<ShopRole>()
                .eq(ShopRole::getShopId,SecurityUtils.getShopUser().getShopId()));
        return ServerResponseEntity.success(list);
    }

    @GetMapping("/info/{roleId}" )
    @ApiOperation(value = "通过id查询角色", notes = "通过id查询角色")
    @ApiImplicitParam(name = "roleId", value = "角色id")
    public ServerResponseEntity<ShopRole> getById(@PathVariable("roleId") Long roleId) {
        ShopRole role = shopRoleService.getById(roleId);
        if (Objects.isNull(role)) {
            throw new YamiShopBindException("yami.shop.data.is.removed");
        }
        if (!Objects.equals(SecurityUtils.getShopUser().getShopId(), role.getShopId())) {
            throw new YamiShopBindException("yami.no.auth");
        }
        //查询角色对应的菜单
        List<Long> menuList = shopMenuService.listMenuIdByRoleId(roleId);
        role.setMenuIdList(menuList);
        return ServerResponseEntity.success(role);
    }

    /**
     * 新增角色
     * @param shopRole 角色
     * @return 是否新增成功
     */
    @SysLog("新增角色" )
    @PostMapping
    @PreAuthorize("@pms.hasPermission('sys:shopRole:save')" )
    @ApiOperation(value = "新增角色", notes = "新增角色")
    public ServerResponseEntity<Boolean> save(@RequestBody ShopRole shopRole) {
        Long employeeId = SecurityUtils.getShopUser().getEmployeeId();
        Long shopId = SecurityUtils.getShopUser().getShopId();
        if (shopRoleService.count(new LambdaQueryWrapper<ShopRole>()
                .eq(ShopRole::getShopId,shopId)
                .eq(ShopRole::getRoleName, shopRole.getRoleName())) > 0) {
            // 系统已存在相同角色名称
            throw new YamiShopBindException("yami.sys.role.identical.name");
        }
        shopRole.setShopId(shopId);
        shopRole.setCreateEmployeeId(employeeId);
        Boolean isOk = shopRoleService.saveRoleAndRoleMenu(shopRole);
        return ServerResponseEntity.success(isOk);
    }

    /**
     * 修改角色
     * @param shopRole 角色
     * @return 是否修改成功
     */
    @SysLog("修改角色" )
    @PutMapping
    @PreAuthorize("@pms.hasPermission('sys:shopRole:update')" )
    @ApiOperation(value = "修改角色", notes = "修改角色")
    public ServerResponseEntity<Boolean> updateById(@RequestBody ShopRole shopRole) {
        int size = shopRoleService.count(new LambdaQueryWrapper<ShopRole>()
                .eq(ShopRole::getShopId, SecurityUtils.getShopUser().getShopId())
                .eq(ShopRole::getRoleName, shopRole.getRoleName())
                .ne(ShopRole::getRoleId, shopRole.getRoleId())
        );
        if (size > 0) {
            // 系统已存在相同角色名称
            throw new YamiShopBindException("yami.sys.role.identical.name");
        }
        Boolean update = shopRoleService.updateRoleAndRoleMenu(shopRole);
        return ServerResponseEntity.success(update);
    }

    /**
     * 通过id删除角色
     * @return 是否删除成功
     */
    @SysLog("删除角色" )
    @DeleteMapping
    @PreAuthorize("@pms.hasPermission('sys:shopRole:delete')" )
    @ApiOperation(value = "通过id删除角色", notes = "通过id删除角色")
    @ApiImplicitParam(name = "roleIds", value = "角色id列表")
    public ServerResponseEntity<Boolean> removeById(@RequestBody Long[] roleIds) {
        Boolean delete = shopRoleService.deleteBatch(roleIds);
        return ServerResponseEntity.success(delete);
    }

}
