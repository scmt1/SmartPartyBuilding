/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yami.shop.user.common.model.Coupon;
import com.yami.shop.user.common.model.UserLevelCoupon;

import java.util.List;

/**
 *
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
public interface UserLevelCouponService extends IService<UserLevelCoupon> {

    /**
     * 批量插入等级优惠券关联信息
     * @param coupons 优惠券id列表
     * @param id 等级id
     */
    void insertBatchCoupon(Long[] coupons, Long id);

    /**
     * 批量删除等级优惠券关联信息
     * @param coupons 优惠券id列表
     * @param id 等级id
     */
    void delBatchCoupon(Long[] coupons, Long id);

    /**
     * 根据等级id，获取优惠券列表信息
     * @param id 等级id
     * @return 优惠券列表
     */
    List<Coupon> getCouponListByLevelId(Long id);
}
