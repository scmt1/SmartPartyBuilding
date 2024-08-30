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
import com.yami.shop.sys.common.model.ShopRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色
 * @author yami
 * @date 2021-03-01 17:42:09
 */
public interface ShopRoleMapper extends BaseMapper<ShopRole> {

    /**
     * 根据ID列表批量删除角色
     * @param roleIds 角色ID列表
     */
    void deleteBatch(@Param("roleIds") List<Long> roleIds);

    /**
     * 根据用户ID获取用户对应的角色列表
     * @param employeeId 用户id
     * @return 角色ID列表
     */
    List<Long> listRoleIdByEmployeeId(@Param("employeeId") Long employeeId);

}
