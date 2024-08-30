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
import com.yami.shop.sys.common.model.ShopRoleMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色与菜单对应关系
 *
 * @author yami
 * @date 2021-03-01 17:42:09
 */
public interface ShopRoleMenuMapper extends BaseMapper<ShopRoleMenu> {

    /**
     * 根据菜单ID批量删除角色与菜单对应关系
     *
     * @param menuId 菜单ID
     */
    void deleteByMenuId(@Param("menuId") Long menuId);

    /**
     * 批量插入角色与菜单对应关系
     *
     * @param roleId     角色ID
     * @param menuIdList 角色与菜单对应关系列表
     */
    void insertRoleAndRoleMenu(@Param("roleId") Long roleId, @Param("menuIdList") List<Long> menuIdList);

    /**
     * 根据角色ID数组，批量删除
     *
     * @param roleIds 角色ID列表
     * @return 成功数量
     */
    int deleteBatch(@Param("roleIds") List<Long> roleIds);
}
