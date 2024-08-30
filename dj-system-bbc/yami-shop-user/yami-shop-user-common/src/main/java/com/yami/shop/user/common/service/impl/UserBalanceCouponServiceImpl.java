/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.common.service.impl;




import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.yami.shop.user.common.dao.UserBalanceCouponMapper;
import com.yami.shop.user.common.model.Coupon;
import com.yami.shop.user.common.model.UserBalanceCoupon;
import com.yami.shop.user.common.service.UserBalanceCouponService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

/**
 *
 *
 * @author YXF
 * @date 2020-09-08 10:42:39
 */
@Service
@AllArgsConstructor
public class UserBalanceCouponServiceImpl extends ServiceImpl<UserBalanceCouponMapper, UserBalanceCoupon> implements UserBalanceCouponService {

    private final UserBalanceCouponMapper userBalanceCouponMapper;


    @Override
    public void insertBatch(Long balanceId, List<Coupon> couponList) {
        List<UserBalanceCoupon> userBalanceCouponList = Lists.newArrayList();
        for (Coupon coupon:couponList){
            userBalanceCouponList.add(new UserBalanceCoupon(balanceId,coupon.getCouponId(),coupon.getCouponNum()));
        }
        saveBatch(userBalanceCouponList);
    }

    @Override
    public void removeByBalaceIdAndCouponId(Long balanceId, Set<Long> couponIdSet) {
        userBalanceCouponMapper.removeByBalaceIdAndCouponId(balanceId,couponIdSet);
    }

    @Override
    public void updateBatchByCoupons(Long balanceId, List<Coupon> couponList) {
        userBalanceCouponMapper.updateBatchByCoupons(balanceId,couponList);
    }

    @Override
    public void removeByBalanceId(Long balanceId) {
        userBalanceCouponMapper.removeByBalanceId(balanceId);
    }
}
