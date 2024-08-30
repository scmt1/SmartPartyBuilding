/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.app.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author LGH
 */
@Data
public class ChangeShopCartParam {

    @ApiModelProperty(value = "购物车ID", required = true)
    private Long basketId;

    @NotNull(message = "商品ID不能为空")
    @ApiModelProperty(value = "商品ID、套餐商品id", required = true)
    private Long prodId;

    @ApiModelProperty(value = "旧的skuId、套餐商品skuId 如果传过来说明在变更sku", required = true)
    private Long oldSkuId;

    @NotNull(message = "skuId不能为空")
    @ApiModelProperty(value = "skuId、套餐商品skuId", required = true)
    private Long skuId;

    @NotNull(message = "店铺ID不能为空")
    @ApiModelProperty(value = "店铺ID", required = true)
    private Long shopId;

    @NotNull(message = "商品个数不能为空")
    @ApiModelProperty(value = "商品个数、套餐个数", required = true)
    private Integer count;

    @ApiModelProperty(value = "分销推广人卡号")
    private String distributionCardNo;

    @ApiModelProperty(value = "套餐id")
    private Long comboId;

    @ApiModelProperty(value = "搭配商品Sku id列表")
    private List<Long> matchingSkuIds;

    @ApiModelProperty(value = "商品是否勾选 true：勾选 ")
    private Boolean isCheck;

    @ApiModelProperty(value = "满减活动id")
    private Long discountId;
}
