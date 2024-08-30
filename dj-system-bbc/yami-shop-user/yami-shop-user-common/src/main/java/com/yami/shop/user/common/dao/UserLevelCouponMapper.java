/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yami.shop.user.common.model.Coupon;
import com.yami.shop.user.common.model.UserLevelCoupon;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 等级优惠券关联
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
public interface UserLevelCouponMapper extends BaseMapper<UserLevelCoupon> {

    /**
     * 批量插入等级优惠券关联信息
     * @param coupons 优惠券id列表
     * @param id 等级id
     */
    void insertBatchCoupon(@Param("coupons") Long[] coupons, @Param("id") Long id);

    /**
     * 批量删除等级优惠券关联信息
     * @param coupons 优惠券id列表
     * @param id 等级id
     */
    void delBatchCoupon(@Param("coupons") Long[] coupons, @Param("id") Long id);

    /**
     * 根据等级id，获取优惠券列表信息
     * @param id 等级id
     * @return 优惠券列表
     */
    List<Coupon> getCouponListByLevelId(@Param("id")Long id);
}
