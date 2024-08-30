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
import com.yami.shop.bean.model.OrderItem;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 退款订单项
 * @author LGH
 */
@Data
public class RefundOrderItemDto {

    @ApiModelProperty("产品名称")
    private String prodName;

    @ApiModelProperty("sku名称")
    private String skuName;

    @ApiModelProperty("商品id")
    private Long prodId;

    @ApiModelProperty("产品图片")
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String pic;

    @ApiModelProperty("产品价格")
    private Double price;

    @ApiModelProperty("物品数量")
    private Integer prodCount;

    @ApiModelProperty("产品总价格")
    private Double productTotalAmount;

    @ApiModelProperty("平台补贴的优惠金额")
    private Double platformShareReduce;

    @ApiModelProperty("商品实际金额")
    private Double actualTotal;

    @ApiModelProperty("赠送主订单项id")
    private Double giveawayOrderItemId;

    @ApiModelProperty(value = "订单项赠品列表", required = true)
    private List<OrderItem> giveawayList;
}
