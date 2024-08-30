/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.dto;

import com.yami.shop.bean.model.OrderItem;
import com.yami.shop.bean.model.OrderRefund;
import com.yami.shop.bean.model.RefundDelivery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Yami
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderRefundDto extends OrderRefund implements Serializable {

    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty("实际总值")
    private Double orderAmount;

    @ApiModelProperty("支付单ID")
    private Long settlementId;

    @ApiModelProperty("支付单号")
    private String orderPayNo;

    @ApiModelProperty("收货地址对象")
    private RefundDelivery refundDelivery;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("订单支付时间")
    private Date orderPayTime;

    @ApiModelProperty("申请类型:1,仅退款,2退款退货")
    private Integer applyType;

    @ApiModelProperty("是否收到货")
    private Boolean isReceiver;

    @ApiModelProperty("订单项")
    private List<OrderItem> orderItems = new ArrayList<>();
    /**
     * 订单退款状态
     */
    private String refundStatus;
    /**
     * 订单状态
     */
    private Integer status;

    @ApiModelProperty("订单类型 1团购订单 2秒杀订单 3积分订单")
    private Integer orderType;

    @ApiModelProperty("true:可以取消退款  false：不可以取消退款（待发货状态->非整单退款->所有商品已经申请退款->不可取消退款）")
    private Boolean isCancel;

    /**
     * 订单类别 0.实物商品订单 1. 虚拟商品订单
     */
    @ApiModelProperty("订单类别 0.实物商品订单 1. 虚拟商品订单")
    private Integer orderMold;
}
