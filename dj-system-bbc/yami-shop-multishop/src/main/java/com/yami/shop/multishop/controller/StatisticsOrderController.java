/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.multishop.controller;


import cn.hutool.core.date.DateUtil;
import com.yami.shop.bean.app.dto.OrderCountData;
import com.yami.shop.bean.dto.StatisticsRefundDto;
import com.yami.shop.bean.param.OrderPayParam;
import com.yami.shop.bean.param.PayTopParam;
import com.yami.shop.bean.param.StatisticsRefundParam;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.OrderItemService;
import com.yami.shop.service.StatisticsOrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
/**
 * @author lhd on 2019/10/17.
 */
@RestController
@RequestMapping("/shop/statisticsOrder")
@AllArgsConstructor
@Api(tags = "订单数据统计接口")
public class StatisticsOrderController {

    private final StatisticsOrderService statisticsOrderService;
    private final OrderItemService orderItemService;

    @GetMapping("/orderCount")
    @ApiOperation(value = "查询店铺订单各状态数量", notes = "查询店铺订单各状态数量")
    public ServerResponseEntity<OrderCountData> getOrderCount() {
        OrderCountData orderCountData = statisticsOrderService.getOrderCountOfStatusByShopId(SecurityUtils.getShopUser().getShopId());
        return ServerResponseEntity.success(orderCountData);
    }

    @GetMapping("/orderPayByShopId")
    @ApiOperation(value = "通过时间获取支付信息")
    public ServerResponseEntity<OrderPayParam> orderPayByShopId() {
        OrderPayParam actualTotal = statisticsOrderService.getPayUserCountByshopId(
                SecurityUtils.getShopUser().getShopId(),DateUtil.beginOfDay(DateUtil.date()), DateUtil.endOfDay(DateUtil.date()));
        return ServerResponseEntity.success(actualTotal);
    }

    @GetMapping("/getActualTotalByHour")
    @ApiOperation(value = "通过24小时分段获取支付金额")
    public ServerResponseEntity<OrderPayParam> getActualTotalByHour() {
        OrderPayParam payList = statisticsOrderService.getActualTotalByHour(
                SecurityUtils.getShopUser().getShopId(),DateUtil.beginOfDay(DateUtil.date()),DateUtil.endOfDay(DateUtil.date()));
        return ServerResponseEntity.success(payList);
    }

    @GetMapping("/getActualTotalByDay")
    @ApiOperation(value = "通过天数分段获取支付金额")
    public ServerResponseEntity<List<OrderPayParam>> getActualTotalByDay() {
        List<OrderPayParam> payList = statisticsOrderService.getActualTotalByDay(
                SecurityUtils.getShopUser().getShopId(),DateUtil.endOfDay(DateUtil.lastMonth()),DateUtil.endOfDay(DateUtil.date()));
        return ServerResponseEntity.success(payList);
    }

    @GetMapping("/getOrderRefundByTime")
    @ApiOperation(value = "通过时间获取比率信息")
    public ServerResponseEntity<StatisticsRefundParam> getOrderRefundByTime() {
        StatisticsRefundParam refundParam = statisticsOrderService.getOrderRefundByShopId(
                SecurityUtils.getShopUser().getShopId(),DateUtil.beginOfDay(DateUtil.date()), DateUtil.endOfDay(DateUtil.date()));
        return ServerResponseEntity.success(refundParam);
    }

    @GetMapping("/getOrderRefundDayByTime")
    @ApiOperation(value = "通过时间获取分段比率信息及退款金额信息")
    public ServerResponseEntity<List<StatisticsRefundParam>> getOrderRefundById() {
        List<StatisticsRefundParam> refundList = statisticsOrderService.getOrderRefundByShopIdAndDay(
                SecurityUtils.getShopUser().getShopId(),DateUtil.endOfDay(DateUtil.lastMonth()),DateUtil.endOfDay(DateUtil.date()));
        return ServerResponseEntity.success(refundList);
    }

    @GetMapping("/getRefundRankingByProd")
    @ApiOperation(value = "根据商品名生成退款排行")
    public ServerResponseEntity<List<StatisticsRefundParam>> getRefundRankingByProd(StatisticsRefundDto statisticsRefundDto) {
        statisticsRefundDto.setShopId(SecurityUtils.getShopUser().getShopId());
        List<StatisticsRefundParam> refundList = statisticsOrderService.getRefundRankingByProd(statisticsRefundDto);
        return ServerResponseEntity.success(refundList);
    }

    @GetMapping("/getRefundRankingByReason")
    @ApiOperation(value = "根据退款原因生成退款排行")
    public ServerResponseEntity<List<StatisticsRefundParam>> getRefundRankingByReason() {
        List<StatisticsRefundParam> refundList = statisticsOrderService.getRefundRankingByReason(
                SecurityUtils.getShopUser().getShopId(),DateUtil.endOfDay(DateUtil.lastMonth()),DateUtil.endOfDay(DateUtil.date()));
        return ServerResponseEntity.success(refundList);
    }


    /**
     * 支付数量TOP
     */
    @GetMapping("/getPayAmountTop")
    @ApiOperation(value = "支付数量TOP", notes = "支付数量TOP")
    public ServerResponseEntity<List<PayTopParam>> getPayAmountTop(StatisticsRefundDto statisticsRefundDto) {
        statisticsRefundDto.setShopId(SecurityUtils.getShopUser().getShopId());
        List<PayTopParam> pages = orderItemService.getOrderProdPayCount(statisticsRefundDto, I18nMessage.getDbLang());
        return ServerResponseEntity.success(pages);
    }

}
