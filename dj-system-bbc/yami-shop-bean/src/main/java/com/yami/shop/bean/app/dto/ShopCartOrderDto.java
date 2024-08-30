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

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 单个店铺的订单信息
 * @author Yami
 */
@Data
public class ShopCartOrderDto implements Serializable{

    @ApiModelProperty(value = "店铺id", required = true)
    private Long shopId;

    @ApiModelProperty(value = "店铺名称", required = true)
    private String shopName;

    @ApiModelProperty(value = "实际总值", required = true)
    private Double actualTotal;

    @ApiModelProperty(value = "商品总值", required = true)
    private Double total;

    @ApiModelProperty(value = "商品总数", required = true)
    private Integer totalCount;

    @ApiModelProperty(value = "商家包邮减免运费金额", required = true)
    private Double freeTransFee = 0.0;

    @ApiModelProperty("平台运费减免金额")
    private Double platformFreeFreightAmount = 0.0;

    @ApiModelProperty(value = "运费", required = true)
    private Double transFee;

    @ApiModelProperty(value = "促销活动优惠金额", required = true)
    private Double discountReduce;

    @ApiModelProperty(value = "优惠券优惠金额", required = true)
    private Double couponReduce = 0.0;

    @ApiModelProperty(value = "平台优惠券优惠金额", required = true)
    private Double platformCouponReduce;

    @ApiModelProperty(value = "积分优惠金额", required = true)
    private Double scoreReduce;

    @ApiModelProperty(value = "使用积分", required = true)
    private Long useScore;

    @ApiModelProperty(value = "平台优惠金额", required = true)
    private Double platformAmount;

    @ApiModelProperty(value = "平台佣金", required = true)
    private Double platformCommission;

    @ApiModelProperty(value = "等级优惠券金额", required = true)
    private Double levelReduce;

    @ApiModelProperty(value = "套餐优惠金额", required = true)
    private Double shopComboAmount;

    @ApiModelProperty(value = "店铺优惠金额(促销活动 + 优惠券 + 积分优惠金额 + 套餐优惠金额 + 其他)", required = true)
    private Double shopReduce = 0.0;

    @ApiModelProperty(value = "订单备注信息", required = true)
    private String remarks;

    @ApiModelProperty(value = "购物车商品（满减）", required = true)
    private List<ShopCartItemDiscountDto> shopCartItemDiscounts;

    @ApiModelProperty(value = "整个店铺可以使用的优惠券列表", required = true)
    private List<CouponOrderDto> coupons;

    @ApiModelProperty(value = "订单编号", required = true)
    private String orderNumber;

    @ApiModelProperty(value = "同城配送可用状态 : 0 不可用 1 可用 -1 不在范围内 -2 商品没有配置同城配送信息 -3 起送费不够", required = true)
    private Integer shopCityStatus;

    @ApiModelProperty(value = "同城配送起送费", required = true)
    private double startDeliveryFee;

    @ApiModelProperty(value = "核销次数 -1.多次核销 0.无需核销 1.单次核销", required = true)
    private Integer writeOffNum;

    @ApiModelProperty(value = "多次核销次数 -1.无限次")
    private Integer writeOffMultipleCount;

    @ApiModelProperty(value = "核销开始时间", required = true)
    private Date writeOffStart;

    @ApiModelProperty(value = "核销结束时间", required = true)
    private Date writeOffEnd;

    @ApiModelProperty(value = "是否可以退款 1.可以 0.不可以", required = true)
    private Integer isRefund;

    @ApiModelProperty(value = "订单类型，0.普通订单", required = true)
    private Integer orderType;
    @ApiModelProperty(value = "立减批发价总值")
    private Double wholesalePriceTotal;

}
