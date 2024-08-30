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


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yami.shop.bean.dto.OrderRefundDto;
import com.yami.shop.bean.model.OrderRefund;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.service.OrderRefundService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author yami
 */
@RestController
@AllArgsConstructor
@RequestMapping("/platform/orderRefund")
@Api(tags = "订单退款")
public class OrderRefundController {

    private final OrderRefundService orderRefundService;

    @GetMapping("/page")
    @PreAuthorize("@pms.hasPermission('platform:orderRefund:page')")
    @ApiOperation(value = "分页查询", notes = "分页查询")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "startTime", value = "开始时间"),
            @ApiImplicitParam(name = "endTime", value = "结束时间")
    })
    public ServerResponseEntity<IPage<OrderRefundDto>> getOrderRefundPage(PageParam<OrderRefundDto> page, OrderRefundDto orderRefundDto,
                                                                    @RequestParam(required = false) String startTime,
                                                                    @RequestParam(required = false) String endTime) {
        IPage<OrderRefundDto> resp = orderRefundService.getPage(page, orderRefundDto, startTime, endTime, 1);
        return ServerResponseEntity.success(resp);
    }

    @GetMapping("/info")
    @PreAuthorize("@pms.hasPermission('platform:orderRefund:info')")
    @ApiOperation(value = "获取订单退款信息", notes = "获取订单退款信息")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "refundId", value = "退款id"),
            @ApiImplicitParam(name = "shopId", value = "店铺id")
    })
    public ServerResponseEntity<OrderRefund> info(@RequestParam("refundId") Long refundId, @RequestParam("shopId") Long shopId) {
        OrderRefund orderRefund = orderRefundService.getOrderRefundById(refundId, shopId);
        return ServerResponseEntity.success(orderRefund);
    }

}
