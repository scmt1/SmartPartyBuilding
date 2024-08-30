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
import com.yami.shop.user.common.dao.UserLevelCouponMapper;
import com.yami.shop.user.common.model.Coupon;
import com.yami.shop.user.common.model.UserLevelCoupon;
import com.yami.shop.user.common.service.UserLevelCouponService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
@Service
@AllArgsConstructor
public class UserLevelCouponServiceImpl extends ServiceImpl<UserLevelCouponMapper, UserLevelCoupon> implements UserLevelCouponService {

    private final UserLevelCouponMapper userLevelCouponMapper;



    @Override
    public void insertBatchCoupon(Long[] coupons ,Long id) {
        userLevelCouponMapper.insertBatchCoupon(coupons, id);
    }

    @Override
    public void delBatchCoupon(Long[] coupons, Long id) {
        userLevelCouponMapper.delBatchCoupon(coupons, id);
    }

    @Override
    public List<Coupon> getCouponListByLevelId(Long id) {
        return userLevelCouponMapper.getCouponListByLevelId(id);
    }

}
