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
import com.yami.shop.bean.model.Product;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author LGH
 */
@Data
public class ProductItemDto implements Serializable {

    @ApiModelProperty(value = "产品名称",required=true)
    private String prodName;

    @ApiModelProperty(value = "产品中文名称",required=true)
    private String prodNameCn;

    @ApiModelProperty(value = "产品英文名称",required=true)
    private String prodNameEn;

    @ApiModelProperty(value = "产品个数",required=true)
    private Integer prodCount;

    @ApiModelProperty(value = "分类id",required=true)
    private Long categoryId;

    @ApiModelProperty(value = "产品图片路径",required=true)
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String pic;

    @ApiModelProperty(value = "产品价格",required=true)
    private Double price;

    @ApiModelProperty(value = "产品套餐单价价格",required=true)
    private Double comboPrice;

    @ApiModelProperty(value = "产品所需积分",required=true)
    private Long scorePrice = 0L;

    @ApiModelProperty(value = "产品购买花费积分",required=true)
    private Integer useScore;

    @ApiModelProperty(value = "商品总金额",required=true)
    private Double productTotalAmount;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty(value = "产品ID",required=true)
    private Long prodId;

    @ApiModelProperty(value = "skuId",required=true)
    private Long skuId;

    @ApiModelProperty(value = "规格名称", required = true)
    private String skuName;

    @ApiModelProperty(value = "basketId",required=true)
    private Long basketId;

    @ApiModelProperty(value = "商品实际金额 = 商品总金额 - 分摊的优惠金额 - 分摊的积分抵现金额")
    private Double actualTotal;

    @ApiModelProperty(value = "分摊的积分抵现金额")
    private Double scorePayReduce;

    @ApiModelProperty(value = "满减满折优惠id，0不主动参与活动（用户没有主动参与该活动），-1主动不参与活动")
    private Long discountId = 0L;

    @ApiModelProperty(value = "分摊的优惠金额")
    private Double shareReduce = 0.0;

    @ApiModelProperty(value = "是否预售订单 1：是 0：不是")
    private Integer preSellStatus = 0;

    @ApiModelProperty(value = "预售发货时间")
    private Date preSellTime;

    @ApiModelProperty(value = "能否分摊优惠券优惠金额(1可以 0不可以)")
    private Integer isShareReduce;

    @ApiModelProperty(value = "能否分摊积分抵现优惠金额(1可以 0不可以)")
    private Integer isScoreReduce;

    @ApiModelProperty(value = "平台分摊的优惠金额")
    private Double platformShareReduce = 0.0;

    @ApiModelProperty("参与满减活动列表")
    private List<DiscountDto> discounts = new ArrayList<>();

    @ApiModelProperty("配送方式json")
    private String deliveryMode;

    @ApiModelProperty("运费模板id")
    private Long deliveryTemplateId;

    @ApiModelProperty("配送方式")
    private Product.DeliveryModeVO deliveryModeVO;

    @ApiModelProperty(value = "重量")
    private Double weight;

    @ApiModelProperty(value = "体积")
    private Double volume;

    @ApiModelProperty(value = "购物车是否勾选")
    private Boolean isChecked;
}
