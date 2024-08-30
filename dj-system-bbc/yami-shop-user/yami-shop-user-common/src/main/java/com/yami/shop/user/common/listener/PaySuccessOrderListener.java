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

import com.yami.shop.bean.event.PaySuccessOrderEvent;
import com.yami.shop.bean.order.PaySuccessOrderOrder;
import com.yami.shop.user.common.dao.UserScoreLockMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 优惠券
 * @author lanhai
 */
@Component("userPaySuccessListener")
@AllArgsConstructor
public class PaySuccessOrderListener {

    private final UserScoreLockMapper userScoreLockMapper;

    /**
     * 更新积分锁为使用状态
     */
    @EventListener(PaySuccessOrderEvent.class)
    @Order(PaySuccessOrderOrder.COMBO)
    public void userPaySuccessListener(PaySuccessOrderEvent event) {
        List<com.yami.shop.bean.model.Order> orders = event.getOrders();
        List<String> orderNumbers = orders.stream().map(com.yami.shop.bean.model.Order::getOrderNumber).collect(Collectors.toList());
        userScoreLockMapper.unLockByOrderNumbers(1, orderNumbers);
//        for (com.yami.shop.bean.model.Order order : orders) {
//            List<UserScoreLock> userScoreLocks = userScoreLockMapper.listUserScoreLocksByOrderNumber(order.getOrderNumber());
//            if (CollectionUtil.isEmpty(userScoreLocks)) {
//                continue;
//            }
//            List<Long> userScoreLockIds = userScoreLocks.stream().map(UserScoreLock::getId).collect(Collectors.toList());
////            int updateStatus = userScoreLockMapper.unLockByIds(1, userScoreLockIds);
//            if (updateStatus == 0) {
//                throw new YamiShopBindException(ResponseEnum.EXCEPTION);
//            }
//        }
    }
}
