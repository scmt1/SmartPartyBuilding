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

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.enums.DeliveryType;
import com.yami.shop.bean.enums.OrderStatus;
import com.yami.shop.bean.event.OrderChangeAddrEvent;
import com.yami.shop.bean.model.Order;
import com.yami.shop.bean.model.OrderItem;
import com.yami.shop.bean.model.OrderVirtualInfo;
import com.yami.shop.bean.model.UserAddrOrder;
import com.yami.shop.bean.param.OrderChangeAddrParam;
import com.yami.shop.bean.param.OrderParam;
import com.yami.shop.bean.vo.UnDeliveryOrderExcelVO;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.response.ResponseEnum;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author lgh on 2018/09/15.
 */
@RestController
@RequestMapping("/order/order")
@Api(tags = "订单接口")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private OrderVirtualInfoService orderVirtualInfoService;

    @Autowired
    private UserAddrOrderService userAddrOrderService;

    @Autowired
    private OrderExcelService orderExcelService;


    @Autowired
    private ApplicationContext applicationContext;

    @GetMapping("/page")
    @ApiOperation(value = "分页获取订单列表", notes = "分页获取订单列表")
    public ServerResponseEntity<IPage<Order>> page(OrderParam orderParam, PageParam<Order> page) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        orderParam.setShopId(shopId);
        orderParam.setLang(I18nMessage.getDbLang());
        IPage<Order> orderPage = orderService.pageOrdersDetailByOrderParam(page, orderParam);
        return ServerResponseEntity.success(orderPage);
    }

    @GetMapping("/orderInfo/{orderNumber}")
    @ApiOperation(value = "根据订单号获取订单信息", notes = "根据订单号获取订单信息")
    public ServerResponseEntity<Order> info(@PathVariable("orderNumber") String orderNumber) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        Order order = orderService.getOrderByOrderNumberAndShopId(orderNumber, shopId, true);
        List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderNumber(orderNumber, I18nMessage.getDbLang());
        order.setOrderItems(orderItems);
        UserAddrOrder userAddrOrder = userAddrOrderService.getById(order.getAddrOrderId());
        order.setUserAddrOrder(userAddrOrder);
        if(Objects.equals(order.getOrderMold(),1)){
            List<OrderVirtualInfo> orderVirtualInfos = orderVirtualInfoService.list(new LambdaQueryWrapper<OrderVirtualInfo>().eq(OrderVirtualInfo::getOrderNumber, orderNumber).eq(OrderVirtualInfo::getIsWriteOff, 0));
            List<String> codes = new ArrayList<>();
            if(CollectionUtil.isNotEmpty(orderVirtualInfos)){
                codes = orderVirtualInfos.stream().map(OrderVirtualInfo::getWriteOffCode).collect(Collectors.toList());
            }
            order.setWriteOffCodes(codes);
        }
        return ServerResponseEntity.success(order);
    }

    @PutMapping("/changeAmount")
    @ApiOperation(value = "修改订单金额", notes = "修改订单金额")
    public ServerResponseEntity<Void> changeAmount(@RequestBody Order order) {
        if (order.getFreightAmount() < 0) {
            // 运费不能小于零
            throw new YamiShopBindException("yami.product.dvy.fee");
        }
        if (Objects.isNull(order.getFreightAmount())) {
            order.setFreightAmount(0.00);
        }
        orderService.changeAmount(order);
        return ServerResponseEntity.success();
    }

    @GetMapping("/getChangeAmount")
    @ApiOperation(value = "查询修改订单地址后的运费")
    public ServerResponseEntity<Double> getChangeAmount(OrderChangeAddrParam order) {
        double changeAmount = 0.0;
        OrderChangeAddrEvent orderChangeAddrEvent = new OrderChangeAddrEvent(order, changeAmount);
        applicationContext.publishEvent(orderChangeAddrEvent);
        return ServerResponseEntity.success(orderChangeAddrEvent.getChangeAmount());
    }

    @PutMapping("/changeUserAddr")
    @ApiOperation(value = "修改用户订单地址", notes = "修改用户订单地址")
    public ServerResponseEntity<String> changeUserAddr(@RequestBody @Valid Order order) {
        Order orderDb = orderService.getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderId, order.getOrderId())
                .eq(Order::getOrderNumber, order.getOrderNumber())
        );
        if (orderDb.getStatus() <= OrderStatus.PADYED.value() || Objects.equals(orderDb.getStatus(), OrderStatus.WAIT_GROUP.value())) {
            UserAddrOrder userAddrOrder = order.getUserAddrOrder();
            userAddrOrderService.updateAddrInfoById(userAddrOrder, orderDb);
        } else {
            // 订单状态异常，无法更改订单地址
            throw new YamiShopBindException("yami.order.status.error");
        }
        // 修改成功
        return ServerResponseEntity.success(ResponseEnum.OK.value(), I18nMessage.getMessage("yami.activity.update.success"));
    }

    @SysLog("修改订单备注")
    @PutMapping("/changeOrderRamark")
    @ApiOperation(value = "修改订单备注", notes = "修改订单备注")
    public ServerResponseEntity<String> changeOrderRamark(@RequestBody @Valid Order order) {
        Order orderDb = orderService.getOne(new LambdaQueryWrapper<Order>()
                .eq(Order::getOrderId, order.getOrderId())
                .eq(Order::getOrderNumber, order.getOrderNumber())
        );
        orderDb.setShopRemarks(order.getShopRemarks());
        orderService.updateById(orderDb);
        // 修改成功
        return ServerResponseEntity.success(ResponseEnum.SHOW_SUCCESS.value(), I18nMessage.getMessage("yami.activity.update.success"));
    }

    @GetMapping("/soldExcel")
    @ApiOperation(value = "导出excel", notes = "导出订单excel")
    public void soldExcel(OrderParam orderParam, HttpServletResponse response) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        orderParam.setShopId(shopId);
        orderExcelService.soldExcel(orderParam, response);
    }

    @GetMapping("/unDeliveryOrderExcel")
    @ApiOperation(value = "导出待发货订单", notes = "导出待发货订单")
    @PreAuthorize("@pms.hasPermission('order:order:exportunorder')")
    public void unDeliveryOrderExcel(OrderParam orderParam, HttpServletResponse response) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        orderParam.setShopId(shopId);
        orderExcelService.unDeliveryOrderExcel(orderParam, response);
    }


}
