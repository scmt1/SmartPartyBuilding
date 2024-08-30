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

import com.yami.shop.bean.vo.OrderDetailVO;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.service.OrderItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Pineapple
 * @date 2021/6/9 9:19
 */
@RestController("multihopOrderItemController")
@RequestMapping("/multishop/order_item")
@Api(tags = "订单项信息接口")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @GetMapping("/get_order_detail")
    @ApiOperation(value = "查询订单项、退款详情", notes = "查询订单项、退款详情")
    public ServerResponseEntity<OrderDetailVO> getOrderItemDetail(String orderNumber, String refundSn){
        OrderDetailVO orderDetailVO = orderItemService.listDetailByParam(orderNumber, refundSn);
        return ServerResponseEntity.success(orderDetailVO);
    }
}
