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
import com.yami.shop.sys.common.model.ShopEmployee;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商家端用户
 * @author person
 * @date 2021-03-01 17:42:09
 */
public interface ShopEmployeeMapper extends BaseMapper<ShopEmployee> {

    /**
     * 根据用户名或手机号获取用户信息以及店铺状态
     * @param username 用户名
     * @return 商家端用户信息
     */
    ShopEmployee getShopByUsernameOrMobile(@Param("username") String username);

    /**
     * 根据用户ID批量获取权限
     * @param employeeId 用户ID
     * @return 权限列表
     */
    List<String> queryAllPerms(@Param("employeeId") Long employeeId);

    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 商家端用户信息
     */
    ShopEmployee getByUserName(@Param("username") String username);

    /**
     * 根据店铺id获取商家账号信息
     * @param shopId
     * @return
     */
    ShopEmployee getMerchantInfoByShopId(@Param("shopId") Long shopId);

    /**
     * 获取员工列表
     * @param shopEmployee
     * @return
     */
    List<ShopEmployee> listByParam(@Param("shopEmployee") ShopEmployee shopEmployee);
}
