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
import com.yami.shop.bean.dto.ShopUserRegisterDto;
import com.yami.shop.bean.model.ShopDetail;
import com.yami.shop.sys.common.model.ShopEmployee;

import java.util.List;

/**
 * 商家端用户
 * @author person
 * @date 2021-03-01 17:42:09
 */
public interface ShopEmployeeService extends IService<ShopEmployee> {

    /**
     * 根据用户ID获取用户信息
     * @param employeeId 用户ID
     * @return 用户信息
     */
    ShopEmployee getShopEmployeeById(Long employeeId);

    /**
     * 根据用户ID更新密码
     * @param employeeId 用户ID
     * @param newPassword 新密码
     * @return 是否成功
     */
    Boolean updatePasswordByEmployeeId(Long employeeId, String newPassword);

    /**
     * 保存用户信息以及保存用户与角色关系
     * @param shopEmployee 用户信息
     * @return 是否成功
     */
    Boolean saveUserAndUserRole(ShopEmployee shopEmployee);

    /**
     * 根据用户名获取用户信息
     * @param username 用户名
     * @return 用户信息
     */
    ShopEmployee getByUserName(String username);

    /**
     * 更新用户以及用户与角色关系
     * @param shopEmployee 用户信息
     * @return 是否成功
     */
    Boolean updateUserAndUserRole(ShopEmployee shopEmployee);

    /**
     * 根据店铺ID检查用户名已存在的数量
     * @param shopId 店铺ID
     * @param mobile 手机号
     * @return 返回数量
     */
    int checkUserName(Long shopId, String mobile);

    /**
     * 更新用户信息
     * @param shopEmployee 用户信息
     * @return 是否成功
     */
    Boolean updatePasswordAndUserName(ShopEmployee shopEmployee);

    /**
     * 更新密码
     * @param employeeId 用户ID
     * @param newPassword 新密码
     * @return 是否成功
     */
    Boolean updatePassword(Long employeeId, String newPassword);

    /**
     * 更新店铺信息以及店铺超级管理员信息
     * @param shopDetail 店铺详细信息
     * @return 是否成功
     */
    Boolean updateUserNameAndPassword(ShopDetail shopDetail);

    /**
     * 根据手机号更新密码
     * @param mobile 手机号
     * @param newPassword 新密码
     * @return 是否成功
     */
    Boolean updatePasswordByUserName(String mobile, String newPassword);

    /**
     * 注册商家账号
     * @param shopUserRegisterDTO
     */
    void registerMerchant(ShopUserRegisterDto shopUserRegisterDTO);

    /**
     * 根据员工id列表删除员工信息
     * @param employeeIds
     */
    void removeEmployeesByIds(List<Long> employeeIds);

    /**
     * 根据店铺id获取店铺商家账号信息
     * @param shopId
     * @return
     */
    ShopEmployee getMerchantInfoByShopId(Long shopId);

    /**
     * 更新商家信息
     * @param shopEmployee
     */
    void updateMerchantInfo(ShopEmployee shopEmployee);

    /**
     * 获取员工列表
     * @param shopEmployee
     * @return
     */
    List<ShopEmployee> listByParam(ShopEmployee shopEmployee);
}
