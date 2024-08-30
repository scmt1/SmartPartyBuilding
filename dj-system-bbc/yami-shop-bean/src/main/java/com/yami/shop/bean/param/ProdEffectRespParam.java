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

/**
 * @author Yami
 */
@Data
public class ProdEffectRespParam {

    @ApiModelProperty(value = "商品id")
    private Long prodId;

    @ApiModelProperty(value = "商品名称")
    private String prodName;

    @ApiModelProperty(value = "商品图片")
    private String prodUrl;

    @ApiModelProperty(value = "商品状态")
    private String prodStatus;

    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "商品价格")
    private Double price = 0.0;

    @ApiModelProperty(value = "曝光次数")
    private Integer expose = 0;

    @ApiModelProperty(value = "曝光人数")
    private Integer exposePersonNum = 0;

    @ApiModelProperty(value = "加购人数")
    private Integer addCartPerson = 0;

    @ApiModelProperty(value = "加购件数")
    private Integer addCart = 0;

    @ApiModelProperty(value = "下单人数")
    private Integer placeOrderPerson = 0;

    @ApiModelProperty(value = "支付人数")
    private Integer payPerson = 0;

    @ApiModelProperty(value = "单品转化率")
    private Double singleProdRate = 0.0;

    @ApiModelProperty(value = "下单商品件数")
    private Integer placeOrderNum = 0;

    @ApiModelProperty(value = "支付商品件数")
    private Integer payNum = 0;

    @ApiModelProperty(value = "商品下单金额")
    private Double placeOrderAmount = 0.0;

    @ApiModelProperty(value = "商品支付金额")
    private Double payAmount = 0.0;

    @ApiModelProperty(value = "申请退款订单数")
    private Integer refundNum = 0;

    @ApiModelProperty(value = "申请退款人数")
    private Integer refundPerson = 0;

    @ApiModelProperty(value = "成功退款订单数")
    private Integer refundSuccessNum = 0;

    @ApiModelProperty(value = "成功退款人数")
    private Integer refundSuccessPerson = 0;

    @ApiModelProperty(value = "成功退款金额")
    private Double refundSuccessAmount = 0.0;

    @ApiModelProperty(value = "退款率")
    private Double refundSuccessRate = 0.0;

}
