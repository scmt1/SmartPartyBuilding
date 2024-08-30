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

import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.multishop.model.YamiShopUser;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.sys.common.model.ShopMenu;
import com.yami.shop.sys.common.service.ShopMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * 菜单管理
 *
 * @author yami
 * @date 2021-03-01 17:42:09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/sys/shopMenu")
@Api(tags = "菜单管理接口")
public class ShopMenuController {

    private final ShopMenuService shopMenuService;

    /**
     * 分页查询
     *
     * @param page     分页对象
     * @param shopMenu 菜单管理
     * @return 分页数据
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页获取菜单", notes = "分页获取菜单")
    public ServerResponseEntity<IPage<ShopMenu>> getShopMenuPage(PageParam<ShopMenu> page, ShopMenu shopMenu) {
        return ServerResponseEntity.success(shopMenuService.page(page, new LambdaQueryWrapper<ShopMenu>()));
    }

    @GetMapping("/nav")
    @ApiOperation(value="获取用户所拥有的菜单和权限", notes="通过登陆用户的userId获取用户所拥有的菜单和权限")
    public ServerResponseEntity<Map<Object, Object>> nav(){
        List<ShopMenu> shopMenus = shopMenuService.listMenuByEmployeeId(SecurityUtils.getShopUser().getEmployeeId());
        //获取用户所以权限
        Set<String> authorities = SecurityUtils.getShopUser().getAuthorities();
        //获取已经隐藏的按钮权限信息
        Set<String> perms = shopMenuService.listConcealButtonPerms();
        //移除掉隐藏按钮的权限信息
        authorities.removeAll(perms);
        return ServerResponseEntity.success(MapUtil.builder().put("menuList", shopMenus).put("authorities", authorities).build());
    }

    /**
     * 获取菜单页面的表
     *
     * @return
     */
    @GetMapping("/table")
    @ApiOperation(value = "获取菜单页面的表", notes = "获取菜单页面的表")
    public ServerResponseEntity<List<ShopMenu>> table() {
        YamiShopUser shopUser = SecurityUtils.getShopUser();
        List<ShopMenu> shopMenus = shopMenuService.listMenuAndBtn(shopUser.getEmployeeId());
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

    /**
     * 选择菜单
     */
    @GetMapping("/listRootMenu")
    @ApiOperation(value = "选择菜单", notes = "选择菜单")
    public ServerResponseEntity<List<ShopMenu>> listRootMenu() {
        //查询列表数据
        List<ShopMenu> shopMenus = shopMenuService.listRootMenu();
        return ServerResponseEntity.success(shopMenus);
    }

    /**
     * 选择子菜单
     */
    @GetMapping("/listChildrenMenu")
    @ApiOperation(value = "选择子菜单", notes = "选择子菜单")
    public ServerResponseEntity<List<ShopMenu>> listChildrenMenu(Long parentId) {
        //查询列表数据
        List<ShopMenu> shopMenus = shopMenuService.listChildrenMenuByParentId(parentId);
        return ServerResponseEntity.success(shopMenus);
    }

    /**
     * 通过id查询菜单管理
     *
     * @param menuId id
     * @return 单个数据
     */
    @GetMapping("/info/{menuId}")
    @ApiOperation(value = "通过id查询菜单", notes = "通过id查询菜单")
    @ApiImplicitParam(name = "menuId", value = "菜单id")
    public ServerResponseEntity<ShopMenu> getById(@PathVariable("menuId") Long menuId) {
        return ServerResponseEntity.success(shopMenuService.getById(menuId));
    }
}
