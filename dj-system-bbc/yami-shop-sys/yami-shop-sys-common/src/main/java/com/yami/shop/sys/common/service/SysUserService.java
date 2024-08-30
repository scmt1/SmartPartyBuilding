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
import com.yami.shop.sys.common.model.SysUser;


/**
 * 系统用户
 * @author lgh
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 修改密码
     * @param userId       用户ID
     * @param newPassword  新密码
     */
    void updatePasswordByUserId(Long userId, String newPassword);

    /**
     * 保存用户与用户角色关系
     * @param user 系统用户信息
     */
    void saveUserAndUserRole(SysUser user);


    /**
     * 更新用户与用户角色关系
     * @param user 系统用户信息
     */
    void updateUserAndUserRole(SysUser user);

    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 系统用户
     */
    SysUser getByUserName(String username);

    /**
     * 根据用户id获取用户信息
     * @param userId 用户ID
     * @return 系统用户
     */
    SysUser getSysUserById(Long userId);

}