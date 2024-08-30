/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.common.listener;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.bean.event.BalanceCouponEvent;
import com.yami.shop.user.common.model.UserBalanceCoupon;
import com.yami.shop.user.common.service.UserBalanceCouponService;
import com.yami.shop.user.common.service.UserBalanceService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 余额优惠券操作
 *
 * @author lhd
 */
@Component("balanceCouponListener")
@AllArgsConstructor
public class BalanceCouponListener {
    private final UserBalanceCouponService userBalanceCouponService;
    private final UserBalanceService userBalanceService;

    @EventListener(BalanceCouponEvent.class)
    public void balanceRefundListener(BalanceCouponEvent event) {
        // 根据优惠券，删除与余额模板的绑定信息
        userBalanceCouponService.remove(new LambdaQueryWrapper<UserBalanceCoupon>().eq(UserBalanceCoupon::getCouponId, event.getCouponId()));
        // 清除余额模板列表的缓存
        userBalanceService.removeCacheByBalanceId();
    }
}
