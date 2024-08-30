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

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.model.Order;
import com.yami.shop.bean.model.OrderItem;
import com.yami.shop.bean.model.UserAddrOrder;
import com.yami.shop.bean.param.OrderParam;
import com.yami.shop.bean.param.OrderPayParam;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * @author lgh on 2018/09/15.
 */
@RestController
@RequestMapping("/platform/order")
@AllArgsConstructor
@Api(tags = "订单")
public class OrderController {

    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final UserAddrOrderService userAddrOrderService;
    private final StatisticsOrderService statisticsOrderService;
    private final OrderExcelService orderExcelService;

    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('platform:order:page')")
    @ApiOperation(value = "分页获取", notes = "分页获取")
    public ServerResponseEntity<IPage<Order>> page(OrderParam orderParam, PageParam<Order> page) {
        orderParam.setLang(I18nMessage.getDbLang());
        IPage<Order> orderPage = orderService.pageOrdersDetailByOrderParam(page, orderParam);
        return ServerResponseEntity.success(orderPage);
    }

    @GetMapping("/orderPayByShopId")
    @ApiOperation(value = "根据商家id获取支付信息", notes = "根据商家id获取支付信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "startTime", value = "开始时间"),
            @ApiImplicitParam(name = "endTime", value = "结束时间")
    })
    public ServerResponseEntity<OrderPayParam> orderPayByShopId(@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")@RequestParam("startTime") Date startTime,
                                                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")@RequestParam("endTime") Date endTime) {
        OrderPayParam actualTotal = statisticsOrderService.getPayUserCountByshopId(null,startTime,endTime);
        return ServerResponseEntity.success(actualTotal);
    }

    @GetMapping("/orderInfo/{orderNumber}")
    @PreAuthorize("@pms.hasPermission('platform:order:info')")
    @ApiOperation(value = "获取信息", notes = "获取信息")
    @ApiImplicitParam(name = "orderNumber", value = "订单编号")
    public ServerResponseEntity<Order> info(@PathVariable("orderNumber") String orderNumber) {

        Order order = orderService.getOne(new LambdaUpdateWrapper<Order>().eq(Order::getOrderNumber, orderNumber));
        if (order == null) {
            // 未找到所在的订单
            throw new YamiShopBindException("yami.order.no.exist");
        }
        UserAddrOrder userAddrOrder = userAddrOrderService.getById(order.getAddrOrderId());
        order.setUserAddrOrder(userAddrOrder);

        List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderNumber(orderNumber, I18nMessage.getDbLang());
        order.setOrderItems(orderItems);
        return ServerResponseEntity.success(order);
    }

    @GetMapping("/soldExcel")
    @PreAuthorize("@pms.hasPermission('platform:order:exportExcel')")
    @ApiOperation(value = "导出已销售订单", notes = "导出已销售订单")
    public void soldExcel(OrderParam orderParam, HttpServletResponse response) {
        orderExcelService.soldExcel(orderParam,response);
    }

    @GetMapping("/getOrderByUserId")
    @ApiOperation(value = "分页获取用户订单列表", notes = "分页获取用户订单列表")
    @ApiImplicitParam(name = "userId", value = "用户id")
    public ServerResponseEntity<IPage<Order>> getOrderByUserId(PageParam<Order> page, String userId){
        IPage<Order> pages = orderService.pageByUserId(page,userId);
        return ServerResponseEntity.success(pages);
    }
}
