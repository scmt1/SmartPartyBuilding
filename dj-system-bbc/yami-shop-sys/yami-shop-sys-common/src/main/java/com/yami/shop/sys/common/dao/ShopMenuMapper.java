/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.sys.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yami.shop.sys.common.model.ShopMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 菜单管理
 *
 * @author yami
 * @date 2021-03-01 17:42:09
 */
public interface ShopMenuMapper extends BaseMapper<ShopMenu> {

    /**
     * 获取所有菜单信息与按钮列表
     *
     * @param lang 当前选择语言
     * @return 菜单信息与按钮列表
     */
    List<ShopMenu> listMenuAndBtn(@Param("lang") Integer lang);

    /**
     * 获取不是按钮类型的菜单列表
     *
     * @param lang 当前选择语言
     * @return 菜单列表
     */
    List<ShopMenu> listSimpleMenuNoButton(@Param("lang") Integer lang);

    /**
     * 获取类型为目录的菜单列表
     *
     * @param lang 当前选择语言
     * @return 菜单列表
     */
    List<ShopMenu> listRootMenu(@Param("lang") Integer lang);

    /**
     * 根据父ID获取所有子菜单列表
     *
     * @param parentId 商品ID
     * @param lang     当前选择语言
     * @return 菜单列表
     */
    List<ShopMenu> listChildrenMenuByParentId(@Param("parentId") Long parentId, @Param("lang") Integer lang);

    /**
     * 查询所有菜单
     *
     * @param lang 当前选择语言
     * @return 菜单列表
     */
    List<ShopMenu> listMenu(@Param("lang") Integer lang);

    /**
     * 查询用户的所有菜单
     *
     * @param employeeId 用户ID
     * @param lang       当前选择语言
     * @return 菜单列表
     */
    List<ShopMenu> listMenuByEmployeeId(@Param("employeeId") Long employeeId, @Param("lang") Integer lang);

    /**
     * 查询角色对应的菜单ID
     *
     * @param roleId
     * @return 菜单ID列表
     */
    List<Long> listMenuIdByRoleId(@Param("roleId") Long roleId);

    List<String> listMenuNameByPerms(@Param("perms") String perms, @Param("lang") Integer lang);

    /**
     * 根据父菜单id修改子菜单和子按钮的是否隐藏状态
     * @param parentId 父id
     * @param state 状态
     */
    int updateSonMenuState(@Param("parentId") Long parentId, @Param("state") Integer state);

    /**
     * 获取隐藏的按钮权限
     * @return
     */
    Set<String> listConcealButtonPerms();

    /**
     * 根据父菜单id获取所以子按钮的权限
     * @param parentId
     * @return
     */
    Set<String> listMenuPerms(@Param("parentId") Long parentId);

    /**
     * 根据店铺员工id获取员工所拥有的菜单权限列表
     * @param lang
     * @param employeeId
     * @return
     */
    List<ShopMenu> listRoleMenuAndBtn(@Param("lang") Integer lang,@Param("employeeId") Long employeeId);
}
