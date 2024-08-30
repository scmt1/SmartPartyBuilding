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

import cn.hutool.core.util.BooleanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.response.ResponseEnum;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.platform.model.YamiSysUser;
import com.yami.shop.security.platform.util.SecurityUtils;
import com.yami.shop.sys.common.constant.MenuType;
import com.yami.shop.sys.common.model.ShopMenu;
import com.yami.shop.sys.common.service.ShopMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Set;


/**
 * 菜单管理
 *
 * @author yami
 * @date 2021-03-01 17:42:09
 */
@Api(tags = "菜单管理")
@RestController
@RequiredArgsConstructor
@RequestMapping("/sys/shopMenu")
public class ShopMenuController {

    private final ShopMenuService shopMenuService;

    @Value("${yami.expose.operation.auth:}")
    private Boolean permission;

    @GetMapping("/page")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public ServerResponseEntity<IPage<ShopMenu>> getShopMenuPage(PageParam<ShopMenu> page, ShopMenu shopMenu) {
        return ServerResponseEntity.success(shopMenuService.page(page, new LambdaQueryWrapper<ShopMenu>()));
    }

    @GetMapping("/table")
    @ApiOperation(value = "获取菜单页面的表", notes = "获取菜单页面的表")
    public ServerResponseEntity<List<ShopMenu>> table() {
        YamiSysUser sysUser = SecurityUtils.getSysUser();
        List<ShopMenu> shopMenus = shopMenuService.listMenuAndBtn(sysUser.getUserId());
        return ServerResponseEntity.success(shopMenus);
    }

    /**
     * 所有菜单列表(用于新建、修改角色时 获取菜单的信息)
     */
    @GetMapping("/list")
    @ApiOperation(value = "获取用户所拥有的菜单(不包括按钮)", notes = "通过登陆用户的userId获取用户所拥有的菜单和权限")
    public ServerResponseEntity<List<ShopMenu>> list() {
        List<ShopMenu> shopMenus = shopMenuService.listSimpleMenuNoButton();
        return ServerResponseEntity.success(shopMenus);
    }

    @GetMapping("/listRootMenu")
    @ApiOperation(value = "获取菜单列表", notes = "获取菜单列表")
    public ServerResponseEntity<List<ShopMenu>> listRootMenu() {
        //查询列表数据
        List<ShopMenu> shopMenus = shopMenuService.listRootMenu();
        return ServerResponseEntity.success(shopMenus);
    }

    @GetMapping("/listChildrenMenu")
    @ApiOperation(value = "获取子菜单列表", notes = "获取子菜单列表")
    @ApiImplicitParam(name = "parentId", value = "上级菜单id")
    public ServerResponseEntity<List<ShopMenu>> listChildrenMenu(Long parentId) {
        //查询列表数据
        List<ShopMenu> shopMenus = shopMenuService.listChildrenMenuByParentId(parentId);
        return ServerResponseEntity.success(shopMenus);
    }

    @GetMapping("/info/{menuId}")
    @ApiOperation(value = "查询菜单管理", notes = "通过id查询菜单管理")
    @ApiImplicitParam(name = "menuId", value = "菜单id")
    public ServerResponseEntity<ShopMenu> getById(@PathVariable("menuId") Long menuId) {
        return ServerResponseEntity.success(shopMenuService.getById(menuId));
    }

    @PostMapping
    @ApiOperation(value = "新增菜单信息", notes = "新增菜单信息")
    public ServerResponseEntity<Boolean> save(@RequestBody @Valid ShopMenu shopMenu) {
        if (BooleanUtil.isFalse(permission)) {
            return ServerResponseEntity.fail(ResponseEnum.NOT_FOUND);
        }
        //数据校验
        verifyForm(shopMenu);
        return ServerResponseEntity.success(shopMenuService.save(shopMenu));
    }

    @PutMapping
    @ApiOperation(value = "修改菜单信息", notes = "修改菜单信息")
    public ServerResponseEntity<String> updateById(@RequestBody @Valid ShopMenu shopMenu) {
        if (BooleanUtil.isFalse(permission)) {
            return ServerResponseEntity.fail(ResponseEnum.NOT_FOUND);
        }
        //数据校验
        verifyForm(shopMenu);
        if (shopMenu.getType() == MenuType.MENU.getValue() && StrUtil.isBlank(shopMenu.getUrl())) {
            // 菜单URL不能为空
            return ServerResponseEntity.showFailMsg(I18nMessage.getMessage("yami.sys.menu.exist"));
        }
        boolean isOk = shopMenuService.updateById(shopMenu);
        //修改子菜单按钮显示隐藏状态
        Set<String> perms = shopMenuService.updateSonMenuState(shopMenu.getMenuId(), shopMenu.getHidden());
        //获取用户的所以菜单权限信息
        Set<String> authorities = SecurityUtils.getSysUser().getAuthorities();
        //隐藏就移除用户对应按钮的权限信息，显示就添加
        if (shopMenu.getHidden() == 1){
            //移除
            authorities.removeAll(perms);
        }else {
            //添加
            authorities.addAll(perms);
        }
        return ServerResponseEntity.success(String.valueOf(isOk));
    }

    @DeleteMapping("/{menuId}")
    @ApiOperation(value = "删除菜单管理", notes = "通过id删除菜单管理")
    @ApiImplicitParam(name = "menuId", value = "菜单id")
    public ServerResponseEntity<String> removeById(@PathVariable Long menuId) {
        if (BooleanUtil.isFalse(permission)) {
            return ServerResponseEntity.fail(ResponseEnum.NOT_FOUND);
        }
        if(menuId <= Constant.SYS_MENU_MAX_ID){
            // 系统菜单，不能删除
            return ServerResponseEntity.showFailMsg(I18nMessage.getMessage("yami.sys.menu.delete.error"));
        }
        //判断是否有子菜单或按钮
        List<ShopMenu> shopMenus = shopMenuService.listChildrenMenuByParentId(menuId);
        if(shopMenus.size() > 0){
            // 请先删除子菜单或按钮
            return ServerResponseEntity.showFailMsg(I18nMessage.getMessage("yami.sys.menu.child.delete"));
        }

        shopMenuService.deleteMenuAndRoleMenu(menuId);
        return ServerResponseEntity.success();
    }

    /**
     * 验证参数是否正确
     */
    private void verifyForm(ShopMenu shopMenu) {

        if (Objects.equals(shopMenu.getMenuId(), shopMenu.getParentId())) {
            // 自己不能是自己的上级
            throw new YamiShopBindException("yami.sys.menu.user.error");
        }
        if (shopMenu.getType() == MenuType.MENU.getValue()) {
            if (StrUtil.isBlank(shopMenu.getUrl())) {
                // 菜单URL不能为空
                throw new YamiShopBindException("yami.sys.menu.exist");
            }
        }

        //上级菜单类型
        int parentType = MenuType.CATALOG.getValue();
        if (shopMenu.getParentId() != 0) {
            ShopMenu parentMenu = shopMenuService.getById(shopMenu.getParentId());
            parentType = parentMenu.getType();
        }

        //目录、菜单
        if (shopMenu.getType() == MenuType.CATALOG.getValue() || shopMenu.getType() == MenuType.MENU.getValue()) {
            if (parentType != MenuType.CATALOG.getValue()) {
                // // 上级菜单不为空时，只能为菜单类型
                throw new YamiShopBindException("yami.sys.menu.superior.list");
            }
            return;
        }

        //按钮
        if (shopMenu.getType() == MenuType.BUTTON.getValue()) {
            if ((parentType != 0) && parentType != MenuType.MENU.getValue()) {
                // 上级菜单不为空时，只能为菜单类型
                throw new YamiShopBindException("yami.sys.menu.superior.menu");
            }
        }
    }
}
