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
import com.yami.shop.user.common.model.UserBalanceCoupon;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @author YXF
 * @date 2020-09-08 10:42:39
 */
public interface UserBalanceCouponMapper extends BaseMapper<UserBalanceCoupon> {

    /**
     * 删除余额、优惠券关联信息
     * @param balanceId  充值余额id
     * @param couponIdSet 优惠券id列表
     */
    void removeByBalaceIdAndCouponId(@Param("balanceId") Long balanceId, @Param("couponIdSet") Set<Long> couponIdSet);

    /**
     * 批量更新余额优惠券关联信息
     * @param balanceId  充值余额id
     * @param couponList 优惠券列表
     */
    void updateBatchByCoupons(@Param("balanceId") Long balanceId, @Param("couponList") List<Coupon> couponList);

    /**
     * 删除余额、优惠券关联信息
     * @param balanceId 充值余额id
     */
    void removeByBalanceId(@Param("balanceId") Long balanceId);
}
