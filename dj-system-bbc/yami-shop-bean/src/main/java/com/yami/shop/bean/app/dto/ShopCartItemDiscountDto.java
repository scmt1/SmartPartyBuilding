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
public class ShopCartItemDiscountDto implements Serializable {

    @ApiModelProperty(value = "已选满减项", required = true)
    private ChooseDiscountItemDto chooseDiscountItemDto;

    @ApiModelProperty(value = "已选套餐项", required = true)
    private ChooseComboItemDto chooseComboItemDto;

    @ApiModelProperty(value = "商品列表")
    private List<ShopCartItemDto> shopCartItems;

    @ApiModelProperty(value = "订单活动类型：0无 1满减 2套餐")
    private Integer activityType;

    public ShopCartItemDiscountDto(){}

    public ShopCartItemDiscountDto(List<ShopCartItemDto> shopCartItems) {
        this.shopCartItems = shopCartItems;
    }
}
