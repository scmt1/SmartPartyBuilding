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

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.yami.shop.bean.event.UserDataEvent;
import com.yami.shop.bean.order.UserDataOrder;
import com.yami.shop.bean.param.UserManagerParam;
import com.yami.shop.common.util.Arith;
import com.yami.shop.user.common.dao.UserBalanceLogMapper;
import com.yami.shop.user.common.dao.UserScoreLogMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;

/**
 * 统计一下用户的信息
 * @author yami
 */
@Slf4j
@Component("UserDataListener")
@AllArgsConstructor
public class UserDataListener {

    private final UserScoreLogMapper userScoreLogMapper;
    private final UserBalanceLogMapper userBalanceLogMapper;

    @EventListener(UserDataEvent.class)
    @Order(UserDataOrder.USER)
    public void userReceiptOrderLister(UserDataEvent event) {
        UserManagerParam param = event.getUserManagerParam();
        String userId = param.getUserId();
        if (StrUtil.isBlank(userId)){
            return;
        }
        // 累计积分
        Integer sumScore = 0;
        try {
            sumScore = userScoreLogMapper.countSumScore(userId);
        } catch (DataIntegrityViolationException e) {
            //如果数值过大，设一个默认值
            sumScore=99999999;
        }

        param.setSumScore(Objects.nonNull(sumScore) ? sumScore : 0);
        // 获取最新充值时间
        Date date = userBalanceLogMapper.getRecentRechargeTime(userId);
        Date reConsTime = param.getReConsTime();
        if (Objects.isNull(reConsTime)) {
            param.setReConsTime(date);
        } else if (Objects.nonNull(date)){
            param.setReConsTime(DateUtil.compare(reConsTime, date)>0?reConsTime: date);
        }
        // 充值余额
        Double rechargeAmount = userBalanceLogMapper.countUserRechargeAmount(userId);
        param.setRechargeAmount(Objects.nonNull(rechargeAmount)?rechargeAmount: 0.0);
        // 充值次数
        param.setRechargeTimes(userBalanceLogMapper.countUserRechargeNum(userId));
        // 实付金额, 累计支付的金额: 除了余额支付/积分支付的订单累计金额 + 余额充值金额
        param.setActualAmount(sum(param.getActualAmount(),param.getRechargeAmount()));
    }

    private Double sum(Double a,Double b) {
        if (Objects.isNull(a) || Objects.isNull(b)) {
            return  0.0;
        }
        return Arith.add(a,b);
    }

}
