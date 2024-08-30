/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.platform.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.enums.DeliveryType;
import com.yami.shop.bean.enums.OrderStatus;
import com.yami.shop.bean.enums.OrderType;
import com.yami.shop.bean.enums.RefundStatusEnum;
import com.yami.shop.bean.model.Order;
import com.yami.shop.bean.model.OrderItem;
import com.yami.shop.bean.model.UserAddrOrder;
import com.yami.shop.bean.param.DeliveryOrderParam;
import com.yami.shop.bean.param.OrderParam;
import com.yami.shop.bean.vo.UnDeliveryOrderExcelVO;
import com.yami.shop.common.config.Constant;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author lgh on 2018/09/15.
 */
@Api(tags = "积分订单")
@RestController
@RequestMapping("/score/order")
public class ScoreOrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private UserAddrOrderService userAddrOrderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SkuService skuService;

    @Autowired
    private OrderExcelService orderExcelService;


    @GetMapping("/page")
    @ApiOperation(value = "分页获取", notes = "分页获取")
    public ServerResponseEntity<IPage<Order>> page(OrderParam orderParam, PageParam<Order> page) {
        orderParam.setShopId(Constant.PLATFORM_SHOP_ID);
        orderParam.setOrderType(OrderType.SCORE.value());
        orderParam.setLang(I18nMessage.getDbLang());
        IPage<Order> orderPage = orderService.pageOrdersDetailByOrderParam(page, orderParam);
        for (Order order : orderPage.getRecords()) {
            order.setShopName("官方店");
        }
        return ServerResponseEntity.success(orderPage);
    }

    @GetMapping("/orderInfo/{orderNumber}")
    @ApiOperation(value = "获取信息", notes = "获取信息")
    @ApiImplicitParam(name = "orderNumber", value = "订单编号")
    public ServerResponseEntity<Order> info(@PathVariable("orderNumber") String orderNumber) {
        Long shopId = Constant.PLATFORM_SHOP_ID;
        Order order = orderService.getOrderByOrderNumberAndShopId(orderNumber,shopId,true);
        List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderNumber(orderNumber, I18nMessage.getDbLang());
        order.setOrderItems(orderItems);
        UserAddrOrder userAddrOrder = userAddrOrderService.getById(order.getAddrOrderId());
        order.setUserAddrOrder(userAddrOrder);
        return ServerResponseEntity.success(order);
    }

    @PutMapping("/delivery")
    @ApiOperation(value = "商品发货", notes = "商品发货")
    public ServerResponseEntity<Void> delivery(@RequestBody DeliveryOrderParam deliveryOrderParam) {
        Long shopId = Constant.PLATFORM_SHOP_ID;
        Order order = orderService.getOrderByOrderNumberAndShopId(deliveryOrderParam.getOrderNumber(),shopId,true);

        if (Objects.equals(order.getRefundStatus(), RefundStatusEnum.APPLY.value())) {
            // 该订单有退款信息正在处理当中，请处理完毕之后再进行发货的操作
            throw new YamiShopBindException("yami.order.delivery.refund.error！");
        }
        if (!Objects.equals(order.getStatus(), OrderStatus.PADYED.value())){
            // 订单不处于待发货状态，无法进行发货
            throw new YamiShopBindException("yami.order.delivery.no.right");
        }

        Order orderParam = new Order();
        orderParam.setOrderId(order.getOrderId());
        orderParam.setDvyId(deliveryOrderParam.getDvyId());
        orderParam.setDvyFlowId(deliveryOrderParam.getDvyFlowId());
        orderParam.setDvyTime(new Date());
        orderParam.setUpdateTime(new Date());
        orderParam.setStatus(OrderStatus.CONSIGNMENT.value());
        orderParam.setUserId(order.getUserId());

        orderService.delivery(orderParam);

        List<OrderItem> orderItems = orderItemService.getOrderItemsByOrderNumber(deliveryOrderParam.getOrderNumber(), I18nMessage.getDbLang());
        for (OrderItem orderItem : orderItems) {
            productService.removeProdCacheByProdId(orderItem.getProdId());
            skuService.removeSkuCacheBySkuId(orderItem.getSkuId(), orderItem.getProdId());
        }
        return ServerResponseEntity.success();
    }

    @GetMapping("/soldExcel")
    @ApiOperation(value = "导出订单", notes = "导出订单")
    public void soldExcel(OrderParam orderParam, HttpServletResponse response) {
        orderParam.setOrderType(OrderType.SCORE.value());
        orderExcelService.soldExcel(orderParam,response);

    }

    /**
     * 导出待发货订单
     * @param orderParam
     * @param response
     */
    @GetMapping("/unDeliveryOrderExcel")
    @ApiOperation(value = "导出待发货订单")
    public void unDeliveryOrderExcel(OrderParam orderParam, HttpServletResponse response) {
        orderParam.setShopId(Constant.PLATFORM_SHOP_ID);
        orderParam.setOrderType(OrderType.SCORE.value());
        orderExcelService.unDeliveryOrderExcel(orderParam, response);
    }
}
