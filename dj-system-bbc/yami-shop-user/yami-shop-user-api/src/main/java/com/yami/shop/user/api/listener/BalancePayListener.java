/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.api.listener;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.yami.shop.bean.app.param.PayInfoParam;
import com.yami.shop.bean.bo.PayInfoResultBO;
import com.yami.shop.bean.enums.PayEntry;
import com.yami.shop.bean.enums.PayStatus;
import com.yami.shop.bean.enums.UserBalanceLogType;
import com.yami.shop.bean.event.BalancePayEvent;
import com.yami.shop.bean.model.PayInfo;
import com.yami.shop.bean.model.UserExtension;
import com.yami.shop.bean.pay.PayInfoDto;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.util.Arith;
import com.yami.shop.service.PayInfoService;
import com.yami.shop.service.UserExtensionService;
import com.yami.shop.user.common.model.UserBalanceLog;
import com.yami.shop.user.common.service.UserBalanceLogService;
import com.yami.shop.user.common.service.UserLevelService;
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
@Component("balanceListener")
@AllArgsConstructor
public class BalancePayListener {

    private final UserBalanceLogService userBalanceLogService;

    private final PayInfoService payInfoService;

    private final UserExtensionService userExtensionService;

    private final UserLevelService userLevelService;

    @EventListener(BalancePayEvent.class)
    @Transactional(rollbackFor = Exception.class)
    public void balanceListener(BalancePayEvent event) {
        PayInfoDto payInfoDTO = event.getPayInfo();
        PayInfoParam payInfoParam = event.getPayInfoParam();
        PayInfo payInfo = payInfoService.getOne(new LambdaQueryWrapper<PayInfo>().eq(PayInfo::getPayNo, payInfoDTO.getPayNo()));
        Double amount = payInfo.getPayAmount();
        // 已经支付
        if (Objects.equals(payInfo.getPayStatus(), PayStatus.PAYED.value()) || Objects.equals(payInfo.getPayStatus(), PayStatus.REFUND.value())) {
            return;
        }
        UserExtension userExtension = userExtensionService.getOne(new LambdaQueryWrapper<UserExtension>().eq(UserExtension::getUserId, payInfo.getUserId()));
        if (Objects.isNull(userExtension)) {
            payInfoParam.setType(2);
            // 您的信息有误，请尝试刷新后再进行操作
            payInfoParam.setMessage(I18nMessage.getMessage("yami.information.is.wrong"));
            return;
        }
        if(Objects.isNull(userExtension.getTotalBalance()) || userExtension.getTotalBalance() < amount) {
            payInfoParam.setType(3);
            // 您的余额不足，请先充值余额
            payInfoParam.setMessage(I18nMessage.getMessage("yami.balance.is.insufficient"));
            return;
        }
        userExtension.setTotalBalance(Arith.sub(userExtension.getTotalBalance(), amount));
        userExtensionService.updateBalanceByVersion(userExtension);
        // 订单金额大于0，则添加余额记录
        if(amount > 0) {
            UserBalanceLog userBalanceLog = new UserBalanceLog();
            userBalanceLog.setUserId(payInfo.getUserId());
            if (Objects.equals(payInfo.getPayEntry(), PayEntry.VIP.value())){
                // 购买会员
                userBalanceLog.setType(UserBalanceLogType.BUY_MEMBER.value());
            } else {
                userBalanceLog.setType(UserBalanceLogType.PAY.value());
            }
            userBalanceLog.setIoType(0);
            userBalanceLog.setCreateTime(new Date());
            userBalanceLog.setChangeBalance(amount);
            userBalanceLog.setPayNo(payInfoDTO.getPayNo());
            userBalanceLogService.save(userBalanceLog);
        }
        PayInfoResultBO payInfoResultBO = new PayInfoResultBO();
        payInfoResultBO.setIsPaySuccess(true);
        payInfoResultBO.setPayNo(payInfo.getPayNo());
        payInfoResultBO.setCallbackContent("余额支付成功");
        payInfoResultBO.setPayAmount(payInfo.getPayAmount());
        payInfoResultBO.setBizPayNo(payInfo.getPayNo());
        if (Objects.equals(payInfo.getPayEntry(), PayEntry.ORDER.value())) {
            // 订单支付回调
            payInfoService.noticeOrder(payInfoResultBO, payInfo);
        } else if (Objects.equals(payInfo.getPayEntry(), PayEntry.VIP.value())){
            // 购买会员回调
            userLevelService.noticeBuyVip(payInfoResultBO, payInfo);
        }
        payInfoParam.setType(1);
    }
}
