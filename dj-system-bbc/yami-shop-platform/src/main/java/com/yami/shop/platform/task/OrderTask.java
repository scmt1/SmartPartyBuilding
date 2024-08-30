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

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.yami.shop.bean.bo.PayInfoResultBO;
import com.yami.shop.bean.enums.OrderStatus;
import com.yami.shop.bean.enums.PayStatus;
import com.yami.shop.bean.enums.SendType;
import com.yami.shop.bean.model.Order;
import com.yami.shop.bean.model.OrderItem;
import com.yami.shop.bean.model.OrderSettlement;
import com.yami.shop.bean.model.PayInfo;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.enums.PayType;
import com.yami.shop.manager.impl.PayManager;
import com.yami.shop.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * @author yami
 */
@Component
public class OrderTask {


    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private OrderService orderService;
    @Autowired
    private NotifyTemplateService notifyTemplateService;
    @Autowired
    private ProductService productService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private PayInfoService payInfoService;
    @Autowired
    private OrderSettlementService orderSettlementService;
    @Autowired
    private PayManager payManager;
    @Autowired
    private NotifyLogService notifyLogService;

    @XxlJob("cancelOrder")
    public void cancelOrder() {
        Date now = new Date();
        logger.info("取消超时未支付订单。。。");
        // 获取30分钟之前未支付的订单
        List<Order> orders = orderService.listUnRefundOrderAndOrderItems(OrderStatus.UNPAY.value(), DateUtil.offsetMinute(now, -30));
        if (CollectionUtil.isEmpty(orders)) {
            return;
        }

        List<Order> cancelOrderList = this.checkOrders(orders);
        orderService.cancelOrders(cancelOrderList);
        // 移除缓存
        this.removeCache(cancelOrderList);

    }

    /**
     * 确认收货15天后执行订单结算
     */
    @XxlJob("orderCommissionSettlement")
    public void orderCommissionSettlement() {
        logger.info("开始执行订单结算任务》》》》》》》》》》》》》》》》》》》》》");
        Date now = new Date();
        // 确认收货15天的订单，进行结算(更新时间为15天前的订单)
        List<Order> orders = orderService.listPendingSettlementOrders(OrderStatus.SUCCESS.value(), DateUtil.beginOfDay(DateUtil.offsetDay(now, -Constant.DISTRIBUTION_SETTLEMENT_TIME)));
        if (CollectionUtil.isEmpty(orders)) {
            return;
        }
        orderService.orderCommissionSettlement(orders);
        // 移除缓存
        this.removeCache(orders);
        logger.info("结束执行订单结算任务》》》》》》》》》》》》》》》》》》》》》");
    }

    /**
     * 订单催付提醒,每1分钟执行发送一次订单未支付的提醒
     */
    @XxlJob("pressPayOrder")
    public void pressPayOrder() {
        Date now = new Date();
        logger.info("执行订单催付提醒");
        // 获取15分钟之前未支付的订单
        List<Order> orders = orderService.listUnRefundOrderAndOrderItems(OrderStatus.UNPAY.value(), DateUtil.offsetMinute(now, -15));
        if (CollectionUtil.isEmpty(orders)) {
            return;
        }
        // 消息推送-订单催付提醒
        // 对相同用户id进行去重
        orders = orders.stream().filter(distinctByKey(Order::getUserId)).collect(Collectors.toList());
        for (Order order : orders) {
            Integer num = notifyLogService.countMsgNum(order.getOrderNumber());
            if (num < 1) {
                //没发过催付消息才会发送
                notifyTemplateService.sendNotifyOfDelivery(order, null, SendType.PRESS_PAY);
            }
        }
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    /**
     * 确认收货
     */
    @XxlJob("confirmOrder")
    public void confirmOrder() {
        Date now = new Date();
        logger.info("系统自动确认收货订单。。。");
        // 获取15天之前等待确认收货的订单
        List<Order> orders = orderService.listUnRefundOrderAndOrderItems(OrderStatus.CONSIGNMENT.value(), DateUtil.offsetDay(now, -15));
        if (CollectionUtil.isEmpty(orders)) {
            return;
        }
        orderService.receiptOrder(orders);
        // 移除缓存
        this.removeCache(orders);
    }

    /**
     * 查询订单，去除已支付的订单
     *
     * @param orders
     */
    private List<Order> checkOrders(List<Order> orders) {
        List<String> orderNumbers = orders.stream().map(Order::getOrderNumber).collect(Collectors.toList());
        // 收集未支付的订单
        List<Order> orderList = new ArrayList<>();
        //获取订单对应的订单结算数据
        List<OrderSettlement> orderSettlementList = orderSettlementService.list(new LambdaQueryWrapper<OrderSettlement>()
                .in(OrderSettlement::getOrderNumber, orderNumbers));
        Map<String, OrderSettlement> orderSettlementMap = orderSettlementList.stream()
                .collect(Collectors.toMap(OrderSettlement::getOrderNumber, orderSettlement -> orderSettlement));
        for (Order order : orders) {
            OrderSettlement orderSettlement = orderSettlementMap.get(order.getOrderNumber());
            if (Objects.isNull(orderSettlement) || Objects.isNull(orderSettlement.getPayType()) || Objects.isNull(orderSettlement.getPayNo())) {
                orderList.add(order);
                continue;
            }
            String bizPayNo = null;
            if (Objects.nonNull(orderSettlement.getPayNo())) {
                PayInfo payInfo = payInfoService.getOne(Wrappers.lambdaQuery(PayInfo.class).eq(PayInfo::getPayNo, orderSettlement.getPayNo()));
                bizPayNo = payInfo.getBizPayNo();
            }
            PayInfoResultBO payInfoResultBO = payManager.getPayInfo(PayType.instance(orderSettlement.getPayType()), orderSettlement.getPayNo(), bizPayNo);
            if (!payInfoResultBO.getIsPaySuccess()) {
                // 根据内部订单号更新order settlement
                PayInfo payInfo = payInfoService.getOne(new LambdaQueryWrapper<PayInfo>().eq(PayInfo::getPayNo, orderSettlement.getPayNo()));
                if (Objects.equals(payInfo.getPayStatus(), PayStatus.UNPAY.value())) {
                    payInfoService.noticeOrder(payInfoResultBO, payInfo);
                }
            } else {
                orderList.add(order);
            }
        }
        return orderList;
    }

    /**
     * 移除缓存
     */
    private void removeCache(List<Order> orders) {
        for (Order order : orders) {
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                productService.removeProdCacheByProdId(orderItem.getProdId());
                skuService.removeSkuCacheBySkuId(orderItem.getSkuId(), orderItem.getProdId());
            }
        }
    }
}
