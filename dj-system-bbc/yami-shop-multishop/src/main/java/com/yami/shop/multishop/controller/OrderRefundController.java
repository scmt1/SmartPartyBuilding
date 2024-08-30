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


import cn.hutool.core.util.StrUtil;
import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.yami.shop.bean.bo.RefundInfoBo;
import com.yami.shop.bean.dto.OrderRefundDto;
import com.yami.shop.bean.dto.RefundSnDto;
import com.yami.shop.bean.enums.OrderStatus;
import com.yami.shop.bean.enums.ReturnMoneyStsType;
import com.yami.shop.bean.model.Order;
import com.yami.shop.bean.model.OrderItem;
import com.yami.shop.bean.model.OrderRefund;
import com.yami.shop.bean.model.RefundInfo;
import com.yami.shop.bean.param.OrderRefundParam;
import com.yami.shop.common.annotation.SysLog;
import com.yami.shop.common.enums.PayType;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.response.ResponseEnum;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.manager.impl.PayManager;
import com.yami.shop.security.multishop.util.SecurityUtils;
import com.yami.shop.service.OrderItemService;
import com.yami.shop.service.OrderRefundService;
import com.yami.shop.service.OrderService;
import com.yami.shop.service.RefundInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author person
 */
@RestController
@AllArgsConstructor
@RequestMapping("/order/refund")
@Api(tags = "订单退款记录接口")
public class OrderRefundController {

    private final OrderRefundService orderRefundService;
    private final PayManager payManager;
    private final OrderItemService orderItemService;
    private final OrderService orderService;
    private final RefundInfoService refundInfoService;

    @GetMapping("/page")
    @ApiOperation(value = "分页获取退款订单", notes = "分页获取退款订单")
    public ServerResponseEntity<IPage<OrderRefundDto>> getOrderRefundPage(PageParam<OrderRefundDto> page, OrderRefundDto orderRefundDto,
                                                                    @RequestParam(required = false) String startTime, @RequestParam(required = false) String endTime) {
        orderRefundDto.setShopId(SecurityUtils.getShopUser().getShopId());
        return ServerResponseEntity.success(orderRefundService.getPage(page, orderRefundDto, startTime, endTime,1));
    }

    @GetMapping("/info/{refundId}")
    @ApiOperation(value = "根据退款订单id获取退款信息", notes = "根据退款订单id获取退款信息")
    public ServerResponseEntity<OrderRefund> getById(@PathVariable("refundId") Long refundId) {
        OrderRefund orderRefund = orderRefundService.getOrderRefundById(refundId, SecurityUtils.getShopUser().getShopId());
        return ServerResponseEntity.success(orderRefund);
    }

    /**
     * 进入这个方法，会出现两种情况：
     * 1. 仅退款，此时商家同意买家的退款申请，执行发放退款的操作
     * 2. 退货退款操作:
     *   2.1)退货退款的第一步，商家允许买家退款的申请，商家进行设置退货地址，不执行发放退款的操作
     *   2.2)退货退款的第二步，当商家收到货之后，同意买家退款，此时需要发放退款，但不会执行这个方法，执行的是下面这个方法
     *   @see com.yami.shop.multishop.controller.OrderRefundController#returnMoney(OrderRefundParam)
     *
     *
     */
    @SysLog("退款处理 -商家处理退款订单")
    @PutMapping("/process")
    @ApiOperation(value = "处理退款订单", notes = "处理退款订单")
    public ServerResponseEntity<Void> processRefundOrder(@RequestBody OrderRefundParam orderRefundParam) {

        // 处理退款操作
        OrderRefundDto orderRefundDto = orderRefundService.processRefundOrder(orderRefundParam, SecurityUtils.getShopUser().getShopId());

        // 仅退款，执行退款操作
        if (Objects.equals(orderRefundDto.getApplyType(), 1)) {
            orderRefundService.submitWxRefund(orderRefundDto);
        }
        if (Objects.equals(orderRefundDto.getReturnMoneySts(), ReturnMoneyStsType.SUCCESS.value())) {
            // 将orderItem.status改成 0 表示可以发货
            OrderItem orderItem = orderItemService.getOne(new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderItemId, orderRefundDto.getOrderItemId()));
            if(Objects.isNull(orderItem)){
                return ServerResponseEntity.success();
            }
            String orderNumber = orderItem.getOrderNumber();
            List<OrderItem> orderItems1 = orderItemService.list(new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderNumber, orderNumber));
            if (orderItems1.size() <=1 ) {
                return ServerResponseEntity.success();
            }
            orderItemService.update(new LambdaUpdateWrapper<OrderItem>()
                    .set(OrderItem::getStatus,0)
                    .eq(OrderItem::getOrderItemId,orderItem.getOrderItemId()));
            // 如果该单是商品项目的所有订单都是已发货，其他的都是退款成功，则可以改成订单项发货状态
            List<OrderItem> orderItems = orderItemService.list(new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderNumber, orderNumber));
            // 如果所有的订单都是申请退款，并且退款成功，则不改变
            int total = 0;
            for (OrderItem item : orderItems) {
                int count = orderRefundService.count(new LambdaQueryWrapper<OrderRefund>()
                        .eq(OrderRefund::getOrderItemId, item.getOrderItemId())
                        .eq(OrderRefund::getReturnMoneySts, 5));
                if (count > 0) {
                    total++;
                }
            }
            // 所有订单都是退款成功
            if (total == orderItems.size()) {
                return ServerResponseEntity.success();
            }
            boolean isOk = true;
            for (OrderItem item : orderItems) {
                if (item.getStatus() != 0) {
                    isOk = false;
                    break;
                }
            }
            // 所有的订单，有已发货的订单

            if (isOk) {
                Order order = new Order();
                order.setOrderId(orderRefundDto.getOrderId());
                // 更新到发货状态
                Date date = new Date();
                order.setStatus(OrderStatus.CONSIGNMENT.value());
                order.setUpdateTime(date);
                order.setDvyTime(date);
                Integer type = null;
                for (OrderItem item : orderItems) {
                    if (Objects.nonNull(item.getDvyType())){
                        type = item.getDvyType();
                    }
                }

                    order.setDvyType(type);
                orderService.updateById(order);
            }

        }
        return ServerResponseEntity.success();
    }

    @SysLog("退款处理 -商家退货处理")
    @PutMapping("/returnMoney")
    @ApiOperation(value = "退款处理-商家退货处理", notes = "退款处理-商家退货处理")
    public ServerResponseEntity<Void> returnMoney(@RequestBody OrderRefundParam orderRefundParam) {
        if (Objects.isNull(orderRefundParam.getIsReceiver())) {
            // 退货状态不能为空
            throw new YamiShopBindException("yami.order.refund.status.exist");
        }
        // 退货处理
        OrderRefundDto orderRefundDto = orderRefundService.returnMoney(orderRefundParam, SecurityUtils.getShopUser().getShopId());
        // 执行退款操作
        orderRefundService.submitWxRefund(orderRefundDto);
        return ServerResponseEntity.success();
    }

    @SysLog("退款处理-发放退款")
    @PutMapping("/refundRequest")
    @ApiOperation(value = "退款处理-发放退款", notes = "退款处理-发放退款")
    public void refundRequest(@RequestBody RefundSnDto refundSn) {
        // 获取退款信息
        OrderRefundDto orderRefundDto = orderRefundService.getOrderRefundByRefundSn(refundSn.getRefundSn());

        if (orderRefundDto == null) {
            // 退款单不存在
            throw new YamiShopBindException("yami.order.refund.exist");
        }

        if (orderRefundDto.getDecisionTime() == null) {
            // 请先确定退款，才能发起退款操作
            throw new YamiShopBindException("yami.order.refund.define");
        }

        if (orderRefundDto.getRefundTime() != null) {
            // 已退款成功，无法再申请退款操作
            throw new YamiShopBindException("yami.order.refund.success");
        }
        if (!Objects.equals(orderRefundDto.getShopId(), SecurityUtils.getShopUser().getShopId())) {
            throw new YamiShopBindException(ResponseEnum.UNAUTHORIZED);
        }
        if (Objects.equals(-1,orderRefundDto.getReturnMoneySts())) {
            // 买家已取消申请
            throw new YamiShopBindException("yami.order.refund.cancel");
        }
        // 提交退款请求
        orderRefundService.submitWxRefund(orderRefundDto);
//        OrderRefundDto orderRefundDto1 = orderRefundService.getOrderRefundByRefundSn(refundSn.getRefundSn());
//        orderRefundService.verifyRefund(orderRefundDto1, refundSn.getRefundSn());
    }

    @RequestMapping("/result/{payType}")
    @ApiOperation(value = "退款结果通知", notes = "退款结果通知")
    public ServerResponseEntity<String> result(HttpServletRequest request, @RequestBody(required = false) String xmlData, @PathVariable Integer payType) throws WxPayException, UnsupportedEncodingException, AlipayApiException {

        RefundInfoBo refundInfoBo = payManager.validateAndGetRefundInfo(request, PayType.instance(payType), xmlData);
        RefundInfo refundInfo = refundInfoService.getOne(new LambdaQueryWrapper<RefundInfo>().eq(RefundInfo::getRefundId, refundInfoBo.getRefundNo()));
        refundInfo.setPayRefundId(refundInfoBo.getBizRefundNo());
        if (!refundInfoBo.getIsRefundSuccess()) {
            if (StrUtil.isNotBlank(refundInfoBo.getCallbackContent())) {
                refundInfo.setCallbackContent(refundInfoBo.getCallbackContent());
                refundInfo.setCallbackTime(new Date());
                refundInfoService.updateById(refundInfo);
            }
            return ServerResponseEntity.success(refundInfoBo.getSuccessString());
        }
        refundInfoService.refundSuccess(refundInfo);
        return ServerResponseEntity.success(refundInfoBo.getSuccessString());
    }

    @GetMapping("/isLastRefund")
    @ApiOperation("是否为最后一单退款")
    public ServerResponseEntity<Boolean> isLastRefund(String refundSn) {
        Long shopId = SecurityUtils.getShopUser().getShopId();
        return ServerResponseEntity.success(orderRefundService.getIsLastRefund(refundSn, shopId));
    }
}
