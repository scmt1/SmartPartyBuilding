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
import com.yami.shop.sys.common.model.SysUser;

import java.util.List;

/**
 * 系统用户
 * @author yami
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 查询用户的所有权限
     * @param userId 用户ID
     * @return 权限列表
     */
    List<String> queryAllPerms(Long userId);

    /**
     * 根据用户名获取管理员用户
     * @param username 用户名
     * @return 系统用户
     */
    SysUser selectByUsername(String username);

}
