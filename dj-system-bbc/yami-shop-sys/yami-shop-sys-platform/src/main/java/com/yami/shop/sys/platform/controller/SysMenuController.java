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

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.response.ResponseEnum;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.security.platform.model.YamiSysUser;
import com.yami.shop.security.platform.util.SecurityUtils;
import com.yami.shop.sys.common.constant.MenuType;
import com.yami.shop.sys.common.model.SysMenu;
import com.yami.shop.sys.common.service.SysMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 系统菜单
 *
 * @author lgh
 */
@Api(tags = "系统菜单")
@RestController
@RequestMapping("/sys/menu")
@RequiredArgsConstructor
public class SysMenuController {

    private final SysMenuService sysMenuService;

    @Value("${yami.expose.operation.auth:}")
    private Boolean permission;


    @GetMapping("/nav")
    @ApiOperation(value = "获取用户所拥有的菜单和权限", notes = "通过登陆用户的userId获取用户所拥有的菜单和权限")
    public ServerResponseEntity<Map<Object, Object>> nav() {
        List<SysMenu> menuList = sysMenuService.listMenuByUserId(SecurityUtils.getSysUser().getUserId());
        //移除用户隐藏掉的按钮权限
        Set<String> authorities = SecurityUtils.getSysUser().getAuthorities();
        authorities.removeAll(
                sysMenuService.listConcealButtonPerms());
        return ServerResponseEntity.success(MapUtil.builder().put("menuList", menuList).put("authorities",  authorities).build());
    }

    @GetMapping("/table")
    @ApiOperation(value = "获取菜单页面的表", notes = "获取菜单页面的表")
    public ServerResponseEntity<List<SysMenu>> table() {
        YamiSysUser sysUser = SecurityUtils.getSysUser();
        List<SysMenu> sysMenuList = sysMenuService.listMenuAndBtn(sysUser.getUserId());
        return ServerResponseEntity.success(sysMenuList);
    }

    /**
     * 所有菜单列表(用于新建、修改角色时 获取菜单的信息)
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取用户所拥有的菜单(不包括按钮)", notes = "通过登陆用户的userId获取用户所拥有的菜单和权限")
    public ServerResponseEntity<List<SysMenu>> list() {
        List<SysMenu> sysMenuList = sysMenuService.listSimpleMenuNoButton();
        return ServerResponseEntity.success(sysMenuList);
    }

    @GetMapping("/listRootMenu")
    @ApiOperation(value = "选择菜单", notes = "选择菜单")
    public ServerResponseEntity<List<SysMenu>> listRootMenu() {
        //查询列表数据
        List<SysMenu> menuList = sysMenuService.listRootMenu();

        return ServerResponseEntity.success(menuList);
    }

    @GetMapping("/listChildrenMenu")
    @ApiOperation(value = "选择子菜单", notes = "选择子菜单")
    @ApiImplicitParam(name = "parentId", value = "父菜单id")
    public ServerResponseEntity<List<SysMenu>> listChildrenMenu(Long parentId) {
        //查询列表数据
        List<SysMenu> menuList = sysMenuService.listChildrenMenuByParentId(parentId);

        return ServerResponseEntity.success(menuList);
    }

    @GetMapping("/info/{menuId}")
    @ApiOperation(value = "菜单信息", notes = "菜单信息")
    @ApiImplicitParam(name = "menuId", value = "菜单id")
    @PreAuthorize("@pms.hasPermission('sys:menu:info')")
    public ServerResponseEntity<SysMenu> info(@PathVariable("menuId") Long menuId) {
        SysMenu menu = sysMenuService.getById(menuId);
        return ServerResponseEntity.success(menu);
    }

    @SysLog("保存菜单")
    @PostMapping
    @ApiOperation(value = "保存菜单", notes = "保存菜单")
    @PreAuthorize("@pms.hasPermission('sys:menu:save')")
    public ServerResponseEntity<Void> save(@Valid @RequestBody SysMenu menu) {
        if (BooleanUtil.isFalse(permission)) {
            return ServerResponseEntity.fail(ResponseEnum.NOT_FOUND);
        }
        //数据校验
        verifyForm(menu);
        sysMenuService.save(menu);
        return ServerResponseEntity.success();
    }

    @SysLog("修改菜单")
    @PutMapping
    @ApiOperation(value = "修改菜单", notes = "修改菜单")
    @PreAuthorize("@pms.hasPermission('sys:menu:update')")
    public ServerResponseEntity<String> update(@Valid @RequestBody SysMenu menu) {
        if (BooleanUtil.isFalse(permission)) {
            return ServerResponseEntity.fail(ResponseEnum.NOT_FOUND);
        }
        //数据校验
        verifyForm(menu);

        if (menu.getType() == MenuType.MENU.getValue()) {
            if (StrUtil.isBlank(menu.getUrl())) {
                // 菜单URL不能为空
                return ServerResponseEntity.showFailMsg(I18nMessage.getMessage("yami.sys.menu.exist"));
            }
        }
        sysMenuService.updateById(menu);
        //修改子菜单按钮的显示隐藏状态
        Set<String> perms = sysMenuService.updateSonMenuState(menu.getMenuId(), menu.getHidden());
        Set<String> authorities = SecurityUtils.getSysUser().getAuthorities();
        if (menu.getHidden() == 1){
            boolean b = authorities.removeAll(perms);
        }else {
            boolean b = authorities.addAll(perms);
        }

        return ServerResponseEntity.success();
    }

    @SysLog("删除菜单")
    @DeleteMapping("/{menuId}")
    @ApiOperation(value = "删除", notes = "删除")
    @ApiImplicitParam(name = "menuId", value = "菜单id")
    @PreAuthorize("@pms.hasPermission('sys:menu:delete')")
    public ServerResponseEntity<String> delete(@PathVariable Long menuId) {
        if (BooleanUtil.isFalse(permission)) {
            return ServerResponseEntity.fail(ResponseEnum.NOT_FOUND);
        }
        if (menuId <= Constant.SYS_MENU_MAX_ID) {
            // 系统菜单，不能删除
            return ServerResponseEntity.showFailMsg(I18nMessage.getMessage("yami.sys.menu.delete.error"));
        }
        //判断是否有子菜单或按钮
        List<SysMenu> menuList = sysMenuService.listChildrenMenuByParentId(menuId);
        if (menuList.size() > 0) {
            // 请先删除子菜单或按钮
            return ServerResponseEntity.showFailMsg(I18nMessage.getMessage("yami.sys.menu.child.delete"));
        }

        sysMenuService.deleteMenuAndRoleMenu(menuId);

        return ServerResponseEntity.success();
    }

    /**
     * 验证参数是否正确
     */
    private void verifyForm(SysMenu menu) {

        if (menu.getType() == MenuType.MENU.getValue()) {
            if (StrUtil.isBlank(menu.getUrl())) {
                // 菜单URL不能为空
                throw new YamiShopBindException("yami.sys.menu.exist");
            }
        }
        if (Objects.equals(menu.getMenuId(), menu.getParentId())) {
            // 自己不能是自己的上级
            throw new YamiShopBindException("yami.sys.menu.user.error");
        }

        //上级菜单类型
        int parentType = MenuType.CATALOG.getValue();
        if (menu.getParentId() != 0) {
            SysMenu parentMenu = sysMenuService.getById(menu.getParentId());
            parentType = parentMenu.getType();
        }

        //目录、菜单
        if (menu.getType() == MenuType.CATALOG.getValue() ||
                menu.getType() == MenuType.MENU.getValue()) {
            if (parentType != MenuType.CATALOG.getValue()) {
                // 上级菜单只能为目录类型
                throw new YamiShopBindException("yami.sys.menu.superior.list");
            }
            return;
        }

        //按钮
        if (menu.getType() == MenuType.BUTTON.getValue()) {
            if ((parentType != 0) && parentType != MenuType.MENU.getValue()) {
                // 上级菜单不为空时，只能为菜单类型
                throw new YamiShopBindException("yami.sys.menu.superior.menu");
            }
        }
    }
}
