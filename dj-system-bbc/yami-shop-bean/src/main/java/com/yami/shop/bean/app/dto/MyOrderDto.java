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

import com.yami.shop.bean.model.OrderVirtualInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Yami
 */
@Data
@ApiModel("我的订单")
public class MyOrderDto {

    @ApiModelProperty(value = "订单项",required=true)
    private List<MyOrderItemDto> orderItemDtos;

    @ApiModelProperty(value = "订单号",required=true)
    private String orderNumber;

    @ApiModelProperty(value = "总价",required=true)
    private Double actualTotal;

    @ApiModelProperty(value = "使用积分",required=true)
    private Integer userScore;

    @ApiModelProperty(value = "订单状态",required=true)
    private Integer status;

    @ApiModelProperty(value = "订单类型(0普通订单 1团购订单 2秒杀订单)",required=true)
    private Integer orderType;

    @ApiModelProperty(value = "订单类别 0.实物商品订单 1. 虚拟商品订单",required=true)
    private Integer orderMold;

    @ApiModelProperty(value = "订单退款状态（1:申请退款 2:退款成功 3:部分退款成功 4:退款失败）",required=true)
    private Integer refundStatus;

    @ApiModelProperty(value = "配送类型 1:快递 2:自提 3：无需快递",required=true)
    private Integer dvyType;

    @ApiModelProperty(value = "店铺名称",required=true)
    private String shopName;

    @ApiModelProperty(value = "店铺id",required=true)
    private Long shopId;

    @ApiModelProperty(value = "订单运费",required=true)
    private Double freightAmount;

    @ApiModelProperty(value = "订单创建时间",required=true)
    private Date createTime;

    @ApiModelProperty(value = "订单支付时间")
    private Date payTime;

    @ApiModelProperty(value = "商品总数",required=true)
    private Integer productNums;

    @ApiModelProperty(value = "用户自提信息")
    private OrderSelfStationDto orderSelfStationDto;

    @ApiModelProperty(value = "用户自提信息")
    private String orderNumbers;

    @ApiModelProperty(value = "用户备注信息")
    private String remarks;

    @ApiModelProperty(value = "秒杀订单秒杀信息")
    private Long seckillId;

    @ApiModelProperty(value = "支付方式 (1:微信小程序支付 2:支付宝 3微信扫码支付 4 微信h5支付 5微信公众号支付 6支付宝H5支付 7支付宝APP支付 8微信APP支付 9余额支付 10全球PayPal支付)", required = true)
    private Integer payType;

    @ApiModelProperty(value = "订单发票id")
    private Long orderInvoiceId;

    @ApiModelProperty(value = "核销码")
    private String writeOffCode;

    @ApiModelProperty(value = "核销次数 -1.多次核销 0.无需核销 1.单次核销")
    private String writeOffNum;

    @ApiModelProperty(value = "核销开始时间")
    private String writeOffStart;

    @ApiModelProperty(value = "核销结束时间")
    private String writeOffEnd;

    @ApiModelProperty(value = "虚拟商品的留言备注")
    private String virtualRemark;

    @ApiModelProperty(value = "虚拟商品信息")
    private List<OrderVirtualInfo> orderVirtualInfoList;

    @ApiModelProperty(value = "自提点id")
    private Long stationId;

    @ApiModelProperty(value = "发货数量")
    private Integer deliveryCount;
}
