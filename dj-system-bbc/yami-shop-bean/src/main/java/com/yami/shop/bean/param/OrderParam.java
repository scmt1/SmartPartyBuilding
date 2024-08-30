/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author Yami
 */
@Data
public class OrderParam {

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    /**
     * 订单状态 参考com.yami.shop.bean.enums.OrderStatus
     */
    @ApiModelProperty("订单状态")
    private Integer status;

    /**
     * 参考orderType
     */
    @ApiModelProperty("订单类型: 1团购订单 2秒杀订单,3积分订单")
    private Integer orderType;

    @ApiModelProperty("订单类别 0.实物商品订单 1. 虚拟商品订单")
    private Integer orderMold;

    @ApiModelProperty("是否已经支付，1：已经支付过，0：，没有支付过")
    private Integer isPayed;

    @ApiModelProperty("订购流水号")
    private String orderNumber;

    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("商品名称")
    private String prodName;

    @ApiModelProperty("收货人姓名")
    private String receiver;

    @ApiModelProperty("收货人手机号")
    private String mobile;

    @ApiModelProperty("物流类型  1:快递 2:自提 3：无需快递 4：同城快递")
    private Integer dvyType;

    @ApiModelProperty("订单退款状态参考refundStatus（1:申请退款 2:退款成功 3:部分退款成功 4:退款失败）")
    private Integer refundStatus;

    @ApiModelProperty("自提点名称")
    private String stationName;

    @ApiModelProperty("支付类型  0:积分支付 1:微信支付 3：支付宝支付 3：余额支付 4:paypal支付")
    private Integer payType;

    @ApiModelProperty("语言")
    private Integer lang;

    @ApiModelProperty("下单时间排序 0倒序 1正序")
    private Integer seq;
}
