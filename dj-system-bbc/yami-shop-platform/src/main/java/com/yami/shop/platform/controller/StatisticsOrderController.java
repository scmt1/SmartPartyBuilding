/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.platform.controller;


import cn.hutool.core.date.DateUtil;
import com.yami.shop.bean.app.dto.OrderCountData;
import com.yami.shop.bean.dto.StatisticsRefundDto;
import com.yami.shop.bean.param.OrderPayParam;
import com.yami.shop.bean.param.StatisticsRefundParam;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.service.StatisticsOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author lhd on 2019/10/19.
 */
@RestController
@RequestMapping("/platform/statisticsOrder")
@AllArgsConstructor
@Api(tags = "platform")
public class StatisticsOrderController {

    private final StatisticsOrderService statisticsOrderService;

    @GetMapping("/orderCount")
    @ApiOperation(value = "查询店铺订单各状态数量", notes = "查询店铺订单各状态数量")
    public ServerResponseEntity<OrderCountData> getOrderCount() {
        OrderCountData orderCountData = statisticsOrderService.getOrderCountOfStatusByShopId(null);
        return ServerResponseEntity.success(orderCountData);
    }

    @GetMapping("/orderPayByShopId")
    @ApiOperation(value = "通过时间获取支付信息")
    public ServerResponseEntity<OrderPayParam> orderPayByShopId() {
        OrderPayParam actualTotal = statisticsOrderService.getPayUserCountByshopId(null,DateUtil.beginOfDay(DateUtil.date()), DateUtil.endOfDay(DateUtil.date()));
        return ServerResponseEntity.success(actualTotal);
    }

    @GetMapping("/getActualTotalByHour")
    @ApiOperation(value = "通过24小时分段获取支付金额")
    public ServerResponseEntity<OrderPayParam> getActualTotalByHour() {
        OrderPayParam payList = statisticsOrderService.getActualTotalByHour(
                null,DateUtil.beginOfDay(DateUtil.date()),DateUtil.endOfDay(DateUtil.date()));
        return ServerResponseEntity.success(payList);
    }

    @GetMapping("/getActualTotalByDay")
    @ApiOperation(value = "通过天数分段获取支付金额")
    public ServerResponseEntity<List<OrderPayParam>> getActualTotalByDay() {
        List<OrderPayParam> payList = statisticsOrderService.getActualTotalByDay(
                null,DateUtil.endOfDay(DateUtil.lastMonth()),DateUtil.endOfDay(DateUtil.date()));
        return ServerResponseEntity.success(payList);
    }

    @GetMapping("/getOrderRefundByTime")
    @ApiOperation(value = "通过时间获取比率信息")
    public ServerResponseEntity<StatisticsRefundParam> getOrderRefundByTime() {
        StatisticsRefundParam refundParam = statisticsOrderService.getOrderRefundByShopId(
                null,DateUtil.beginOfDay(DateUtil.date()), DateUtil.endOfDay(DateUtil.date()));
        return ServerResponseEntity.success(refundParam);
    }

    @GetMapping("/getOrderRefundDayByTime")
    @ApiOperation(value = "通过时间获取分段比率信息及退款金额信息")
    public ServerResponseEntity<List<StatisticsRefundParam>> getOrderRefundById() {
        List<StatisticsRefundParam> refundList = statisticsOrderService.getOrderRefundByShopIdAndDay(
                null,DateUtil.endOfDay(DateUtil.lastMonth()),DateUtil.endOfDay(DateUtil.date()));
        return ServerResponseEntity.success(refundList);
    }

    @GetMapping("/getRefundRankingByProd")
    @ApiOperation(value = "根据商品名生成退款排行")
    public ServerResponseEntity<List<StatisticsRefundParam>> getRefundRankingByProd() {
        /*
         * 如果一个订单有多个商品的，整单退款，用户输入退款金额少于支付金额，
         * 无法确认某个商品的准确退款金额，所以就用整个订单来代替
         * 例如：订单中 商品A 100 元，商品B 8元，共支付108元，整单退款，退款金额为90元，
         * 所以商品的退款金额在这个时候是模糊的，不能准确统计
         */
        List<StatisticsRefundParam> refundList = statisticsOrderService.getRefundRankingByProd(new StatisticsRefundDto());
        return ServerResponseEntity.success(refundList);
    }

    @GetMapping("/getRefundRankingByReason")
    @ApiOperation(value = "根据退款原因生成退款排行")
    public ServerResponseEntity<List<StatisticsRefundParam>> getRefundRankingByReason() {
        List<StatisticsRefundParam> refundList = statisticsOrderService.getRefundRankingByReason(
                null,DateUtil.endOfDay(DateUtil.lastMonth()),DateUtil.endOfDay(DateUtil.date()));
        return ServerResponseEntity.success(refundList);
    }

}
