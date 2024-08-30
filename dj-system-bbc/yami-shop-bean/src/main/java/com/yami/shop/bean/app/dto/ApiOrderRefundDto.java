/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.app.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Yami
 */
@Data
@ApiModel("退款订单对象")
public class ApiOrderRefundDto {

    @ApiModelProperty("记录ID")
    private Long refundId;

    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty("订单总金额")
    private BigDecimal orderAmount;

    @ApiModelProperty("退款编号")
    private String refundSn;

    @ApiModelProperty("第三方退款单号(微信/支付宝退款单号)")
    private String orderPayNo;

    @ApiModelProperty("订单支付方式(1 微信支付 2 支付宝)")
    private Integer payType;

    @ApiModelProperty("订单支付名称")
    private String payTypeName;

    @ApiModelProperty("退货数量")
    private Integer goodsNum;

    @ApiModelProperty("退款金额")
    private Double refundAmount;

    @ApiModelProperty("退还积分")
    private Integer refundScore;

    @ApiModelProperty("申请类型:1,仅退款,2退款退货")
    private Integer applyType;

    @ApiModelProperty(value = "退款单类型（1:整单退款,2:单个物品退款）")
    private Integer refundType;

    @ApiModelProperty("处理退款状态:(1.买家申请 2.卖家接受 3.买家发货 4.卖家收货 5.退款成功 6.买家撤回申请 7.商家拒绝 -1.退款关闭)")
    private Integer returnMoneySts;

    @ApiModelProperty(value = "最大退款金额")
    private Double maxRefundAmount;

    @ApiModelProperty("申请时间")
    private Date applyTime;

    @ApiModelProperty("卖家处理时间")
    private Date handelTime;

    @ApiModelProperty("退款时间")
    private Date refundTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("订单支付时间")
    private Date orderPayTime;

    @ApiModelProperty("用户退货发货时间")
    private Date shipTime;

    @ApiModelProperty("卖家收到用户退货的货物时间")
    private Date receiveTime;

    @ApiModelProperty("撤销时间")
    private Date cancelTime;

    @ApiModelProperty("同意退款时间")
    private Date decisionTime;

    @ApiModelProperty("卖家拒绝时间")
    private Date rejectTime;

    @ApiModelProperty("文件凭证json")
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String photoFiles;

    @ApiModelProperty("收货地址对象")
    private RefundDeliveryDto refundDelivery;

    @ApiModelProperty("拒绝原因")
    private String rejectMessage;

    @ApiModelProperty("申请原因")
    private String buyerReason;

    @ApiModelProperty("申请说明")
    private String buyerDesc;

    @ApiModelProperty("订单项")
    private List<RefundOrderItemDto> orderItems = new ArrayList<>();

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("店铺ID")
    private Long shopId;

    @ApiModelProperty("true:可以取消退款  false：不可以取消退款")
    private Boolean isCancel;

    @ApiModelProperty("卖家备注")
    private String sellerMsg;

    @NotNull(message = "是否接收到商品(1:已收到,0:未收到)")
    private Boolean isReceiver;

    @ApiModelProperty("物流信息")
    private DeliveryDto deliveryDto;

    @ApiModelProperty("订单类型")
    private Integer orderType;

    /*
ac
    @ApiModelProperty("物流公司名称")
    private String expressName;

    @ApiModelProperty("物流单号")
    private String expressNo;

    @ApiModelProperty("发货时间")
    private Date shipTime;

    @ApiModelProperty("收货时间")
    private Date receiveTime;

    @ApiModelProperty("撤销时间")
    private Date cancelTime;

    @ApiModelProperty("决定时间")
    private Date decisionTime;

    @ApiModelProperty("退款申请状态(0:未申请 1:申请中 2:已完成 -1:失败)")
    private Integer refundApplySts;

    @ApiModelProperty("退款物流信息")
    private RefundDelivery refundDelivery;*/

}
