/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.sys.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.sys.common.model.ShopMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 菜单管理
 * @author yami
 * @date 2021-03-01 17:42:09
 */
public interface ShopMenuService extends IService<ShopMenu> {

    /**
     * 获取所有菜单信息与按钮列表
     * @param employeeId 用户id
     * @return 菜单信息与按钮列表
     */
    List<ShopMenu> listMenuAndBtn(Long employeeId);

    /**
     * 获取不是按钮类型的菜单列表
     * @return 菜单列表
     */
    List<ShopMenu> listSimpleMenuNoButton();

    /**
     * 获取类型为目录的菜单列表
     * @return 菜单列表
     */
    List<ShopMenu> listRootMenu();

    /**
     * 根据父ID获取所有子菜单列表
     * @param parentId 父ID
     * @return 菜单列表
     */
    List<ShopMenu> listChildrenMenuByParentId(Long parentId);

    /**
     * 根据菜单ID删除菜单以及删除菜单与角色关联关系
     * @param menuId 菜单ID
     */
    void deleteMenuAndRoleMenu(Long menuId);

    /**
     * 根据用户ID获取菜单列表
     * @param employeeId 用户ID
     * @return 菜单列表
     */
    List<ShopMenu> listMenuByEmployeeId(Long employeeId);

    /**
     * 查询角色对应的菜单ID
     * @param roleId 角色ID
     * @return 菜单ID列表
     */
    List<Long> listMenuIdByRoleId(Long roleId);

    /**
     * 根据父菜单id修改子菜单和子按钮的是否隐藏状态，返回子按钮的权限信息
     * @param parentId 父id
     * @param state 状态
     * @return 子按钮权限信息
     */
    Set<String> updateSonMenuState(@Param("parentId") Long parentId, @Param("state") Integer state);

    /**
     * 获取隐藏的按钮权限
     * @return
     */
    Set<String> listConcealButtonPerms();


}
