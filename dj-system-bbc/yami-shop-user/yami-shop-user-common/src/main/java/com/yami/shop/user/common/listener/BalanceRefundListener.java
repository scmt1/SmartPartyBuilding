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
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.yami.shop.bean.dto.OrderRefundDto;
import com.yami.shop.bean.enums.RefundStatusEnum;
import com.yami.shop.bean.event.BalanceRefundEvent;
import com.yami.shop.bean.model.RefundInfo;
import com.yami.shop.bean.model.UserExtension;
import com.yami.shop.bean.pay.RefundInfoDto;
import com.yami.shop.common.util.Arith;
import com.yami.shop.service.NotifyTemplateService;
import com.yami.shop.service.OrderRefundService;
import com.yami.shop.service.RefundInfoService;
import com.yami.shop.service.UserExtensionService;
import com.yami.shop.user.common.model.UserBalanceLog;
import com.yami.shop.user.common.service.UserBalanceLogService;
import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

/**
 * 余额支付操作
 *
 * @author lhd
 */
@Component("balanceRefundListener")
@AllArgsConstructor
public class BalanceRefundListener {
    private final UserExtensionService userExtensionService;
    private final UserBalanceLogService userBalanceLogService;
    private final OrderRefundService orderRefundService;
    private final NotifyTemplateService notifyTemplateService;
    private final RefundInfoService refundInfoService;

    /**
     * 余额支付操作
     */
    @EventListener(BalanceRefundEvent.class)
    @Transactional(rollbackFor = Exception.class)
    public void balanceRefundListener(BalanceRefundEvent event) {
        RefundInfoDto eventRefundInfo = event.getRefundInfo();
        int refundCount = userBalanceLogService.count(new LambdaQueryWrapper<UserBalanceLog>().eq(UserBalanceLog::getRefundSn, eventRefundInfo.getRefundSn()));
        if (refundCount > 0) {
            return;
        }
        if (eventRefundInfo.getRefundAmount() > 0) {
            UserBalanceLog userBalanceLog = new UserBalanceLog();
            userBalanceLog.setUserId(eventRefundInfo.getUserId());
            userBalanceLog.setType(4);
            userBalanceLog.setPayNo(eventRefundInfo.getPayNo());
            userBalanceLog.setIoType(1);
            userBalanceLog.setChangeBalance(eventRefundInfo.getRefundAmount());
            userBalanceLog.setCreateTime(new Date());
            userBalanceLogService.save(userBalanceLog);
        }

        UserExtension userExtension = userExtensionService.getOne(new LambdaQueryWrapper<UserExtension>().eq(UserExtension::getUserId, eventRefundInfo.getUserId()));
        userExtension.setTotalBalance(Arith.add(userExtension.getTotalBalance(), eventRefundInfo.getRefundAmount()));
        userExtension.setUpdateTime(new Date());
        userExtensionService.updateBalanceByVersion(userExtension);
        OrderRefundDto orderRefund = orderRefundService.getOrderRefundByRefundSn(eventRefundInfo.getRefundSn());
        if (Objects.isNull(orderRefund)) {
            return;
        }
        RefundInfo refundInfo = refundInfoService.getOne(Wrappers.lambdaQuery(RefundInfo.class).eq(RefundInfo::getRefundId, eventRefundInfo.getRefundSn()));
        if (Objects.equals(refundInfo.getRefundStatus(), RefundStatusEnum.SUCCEED.value())) {
            return;
        }
        refundInfo.setCallbackContent(refundInfo.getCallbackContent());
        refundInfo.setCallbackTime(new Date());
        refundInfo.setRefundStatus(RefundStatusEnum.SUCCEED.value());
        refundInfoService.update(refundInfo, Wrappers.lambdaUpdate(RefundInfo.class).eq(RefundInfo::getRefundId, eventRefundInfo.getRefundSn()));
        orderRefundService.verifyRefund(refundInfo, refundInfo.getPayRefundId());
        // 发送订单退款到余额提醒
        notifyTemplateService.sendOrderRefundToBalanceNotify(orderRefund,eventRefundInfo.getRefundAmount());
    }
}
