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
import com.yami.shop.sys.common.model.ShopEmployeeRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户与角色对应关系
 *
 * @author person
 * @date 2021-03-01 17:42:09
 */
public interface ShopEmployeeRoleMapper extends BaseMapper<ShopEmployeeRole> {

    /**
     * 根据角色ID数组，批量删除
     *
     * @param roleIds 角色ID
     * @return 成功数量
     */
    int deleteBatch(@Param("roleIds") List<Long> roleIds);

    /**
     * 批量插入用户与角色对应关系
     *
     * @param employeeId 用户ID
     * @param roleIdList 角色列表
     * @return 成功数量
     */
    int insertUserAndUserRole(@Param("employeeId") Long employeeId, @Param("roleIdList") List<Long> roleIdList);

    /**
     * 根据用户ID删除用户与角色对应关系
     *
     * @param employeeId 用户ID
     * @return 成功数量
     */
    int deleteByEmployeeId(@Param("employeeId") Long employeeId);

    /**
     * 根据用户ID列表批量删除
     *
     * @param employeeIds
     */
    void deleteByEmployeeIds(@Param("employeeIds") List<Long> employeeIds);

    /**
     * 获取使用过中的角色id列表
     *
     * @param roleIds
     */
    List<Long> selectUseRoleIds(@Param("roleIds") List<Long> roleIds);
}
