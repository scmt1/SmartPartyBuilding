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
import java.util.List;

/**
 * @author Yami
 */
@Data
public class ShopCartDto implements Serializable {

    @ApiModelProperty(value = "店铺ID", required = true)
    private Long shopId;

    @ApiModelProperty(value = "店铺名称", required = true)
    private String shopName;

    @ApiModelProperty(value = "同城配送启用状态 :  1启用 0未启用 ")
    private Integer shopCityStatus;

    @ApiModelProperty(value = "购物车满减活动携带的商品", required = true)
    private List<ShopCartItemDiscountDto> shopCartItemDiscounts;

    @ApiModelProperty("店铺类型1自营店 2普通店")
    private Integer shopType;

    @ApiModelProperty(value = "实际总值(商品总值 - 优惠)", required = true)
    private Double actualTotal;

    @ApiModelProperty(value = "商品总值", required = true)
    private Double total;

    @ApiModelProperty(value = "商品积分总值")
    private Long scoreTotal;

    @ApiModelProperty(value = "店铺优惠金额(促销活动 + 优惠券 + 积分优惠金额 + 其他)", required = true)
    private Double shopReduce;

    @ApiModelProperty(value = "促销活动优惠金额", required = true)
    private Double discountReduce;

    @ApiModelProperty(value = "套餐优惠金额", required = true)
    private Double comboReduce;

    @ApiModelProperty(value = "整个店铺可以使用的优惠券列表", required = true)
    private List<CouponOrderDto> coupons;

    @ApiModelProperty(value = "优惠券优惠金额", required = true)
    private Double couponReduce;

    @ApiModelProperty(value = "数量", required = true)
    private Integer totalCount;

    @ApiModelProperty(value = "运费", required = true)
    private Double transFee;

    @ApiModelProperty(value = "等级免运费金额", required = true)
    private Double freeTransFee;
}
