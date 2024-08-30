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
import com.yami.shop.sys.common.model.SysUserRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户与角色对应关系
 *
 * @author yami
 */
public interface SysUserRoleMapper extends BaseMapper<SysUserRole> {

    /**
     * 根据角色ID数组，批量删除
     *
     * @param roleIds 角色ID列表
     * @return 成功数量
     */
    int deleteBatch(@Param("roleIds") List<Long> roleIds);

    /**
     * 根据用户id删除用户与角色关系
     *
     * @param userId 用户ID
     */
    void deleteByUserId(Long userId);

    /**
     * 根据用户id 批量添加用户角色关系
     *
     * @param userId     用户ID
     * @param roleIdList 角色ID列表
     */
    void insertUserAndUserRole(@Param("userId") Long userId, @Param("roleIdList") List<Long> roleIdList);

    /**
     * 获取使用过中的角色id列表
     *
     * @param roleIds
     */
	List<Long> selectUseRoleIds(@Param("roleIds") List<Long> roleIds);
}
