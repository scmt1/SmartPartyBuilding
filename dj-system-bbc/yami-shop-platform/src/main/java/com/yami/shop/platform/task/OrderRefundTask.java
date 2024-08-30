/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.platform.task;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yami.shop.bean.dto.OrderRefundDto;
import com.yami.shop.bean.enums.ReturnMoneyStsType;
import com.yami.shop.bean.enums.SendType;
import com.yami.shop.bean.model.OrderRefund;
import com.yami.shop.common.config.Constant;
import com.yami.shop.service.NotifyTemplateService;
import com.yami.shop.service.OrderRefundService;
import lombok.extern.slf4j.Slf4j;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author yami
 */
@Slf4j
@Component
public class OrderRefundTask {

    @Autowired
    private OrderRefundService orderRefundService;
    @Autowired
    private NotifyTemplateService notifyTemplateService;
    @Autowired
    private MapperFacade mapperFacade;



    /**
     * 取消申请超时的订单，无论该超时订单处于任何状态
     */
    @XxlJob("cancelWhenTimeOut")
    public void cancelWhenTimeOut() {
        log.info("==============订单退款超时处理开始===================");
        // 设定时间值
        Date date = DateUtil.offsetDay(new Date(), -Constant.MAX_REFUND_APPLY_TIME);
        // 获取待处理的退款订单
        List<OrderRefund> orderRefundList = orderRefundService.list(new LambdaQueryWrapper<OrderRefund>()
                .in(OrderRefund::getReturnMoneySts, Arrays.asList(ReturnMoneyStsType.APPLY.value(),ReturnMoneyStsType.PROCESSING.value(),ReturnMoneyStsType.CONSIGNMENT.value(),ReturnMoneyStsType.RECEIVE.value()))
                .lt(OrderRefund::getApplyTime, date));
        if (CollectionUtils.isNotEmpty(orderRefundList)) {
            orderRefundService.cancelWhenTimeOut(orderRefundList);
        }
        log.info("==============订单退款超时处理结束===================");
    }

    /**
     * 退款临近超时提醒,每12小时执行发送一次的提醒
     */
    @XxlJob("pressRefundOrder")
    public void pressRefundOrder(){
        log.info("==============订单退款超时提醒开始===================");
        // 临时超时时间为 最大申请时间 - 12小时
        Integer overTime = Constant.MAX_REFUND_APPLY_TIME * 24;
        Date date = DateUtil.offsetHour(new Date(), Constant.MAX_REFUND_HOUR - overTime);
        Date overDate = DateUtil.offsetDay(new Date(), -Constant.MAX_REFUND_APPLY_TIME);
        // 获取临近超时的退款订单,大于超时时间，小于临时时间
        List<OrderRefund> orderRefundList = orderRefundService.list(new LambdaQueryWrapper<OrderRefund>()
                .in(OrderRefund::getReturnMoneySts, Arrays.asList(ReturnMoneyStsType.APPLY.value(),ReturnMoneyStsType.PROCESSING.value(),ReturnMoneyStsType.CONSIGNMENT.value(),ReturnMoneyStsType.RECEIVE.value()))
                .gt(OrderRefund::getApplyTime,overDate)
                .lt(OrderRefund::getApplyTime, date));
        if (CollectionUtils.isNotEmpty(orderRefundList)) {
            List<OrderRefundDto> orderRefundDtos = mapperFacade.mapAsList(orderRefundList, OrderRefundDto.class);
            orderRefundDtos = orderRefundDtos.stream().filter(distinctByKey(OrderRefundDto::getUserId)).collect(Collectors.toList());
            for (OrderRefundDto orderRefundDto : orderRefundDtos) {
                notifyTemplateService.sendNotifyByRefund(orderRefundDto,SendType.REFUND_OUT_TIME);
            }
        }
        log.info("==============退款临近超时提醒结束===================");
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }
}
