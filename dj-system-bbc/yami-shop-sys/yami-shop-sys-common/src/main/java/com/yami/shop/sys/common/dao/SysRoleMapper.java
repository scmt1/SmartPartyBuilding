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
import com.yami.shop.sys.common.model.SysRole;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 角色管理
 * @author yami
 */
public interface SysRoleMapper extends BaseMapper<SysRole>{

    /**
     * 根据系统角色ID列表批量删除角色
     * @param roleIds 系统角色ID列表
     */
    void deleteBatch(@Param("roleIds") List<Long> roleIds);

    /**
     * 根据用户ID获取对应的角色ID列表
     * @param userId 用户ID
     * @return 系统角色ID列表
     */
    List<Long> listRoleIdByUserId(Long userId);

}
