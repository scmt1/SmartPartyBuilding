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

import com.yami.shop.bean.event.CancelOrderEvent;
import com.yami.shop.bean.order.CancelOrderOrder;
import com.yami.shop.user.common.service.UserScoreLockService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * 取消订单事件监听
 * @author yami
 */
@Slf4j
@Component("userCancelOrderListener")
@AllArgsConstructor
public class CancelOrderListener {

    final private UserScoreLockService userScoreLockService;


    /**
     * 取消订单积分抵现花费的积分回退
     */
    @EventListener(CancelOrderEvent.class)
    @Order(CancelOrderOrder.SCORE)
    public void userCancelOrderListener(CancelOrderEvent event) {
        com.yami.shop.bean.model.Order order = event.getOrder();
        userScoreLockService.unlock(order);
//        //如果是积分订单无需
//        // 进行此操作
//        if(Objects.equals(order.getOrderType() , OrderType.SCORE.value())){
//            return;
//        }
//        UserScoreLog userScoreLog = userScoreLogService.getOne(new LambdaQueryWrapper<UserScoreLog>()
//                                                        .eq(UserScoreLog::getBizId, order.getOrderNumber())
//                                                        .eq(UserScoreLog::getUserId,order.getUserId())
//                                                        .eq(UserScoreLog::getIoType,0)
//                                                        .eq(UserScoreLog::getSource,ScoreLogType.SCORE_CASH.value()));
//        //判断此笔订单是否有记录，有进行回退用户积分,并添加日志
//        if(userScoreLog != null){
//            userScoreDetailService.updateLogAndDetail(order.getOrderNumber(),order.getUserId());
//        }
    }

}
