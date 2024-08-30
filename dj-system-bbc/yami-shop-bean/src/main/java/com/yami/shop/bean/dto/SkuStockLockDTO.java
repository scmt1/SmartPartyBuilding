/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author FrozenWatermelon
 * @date 2020/12/22
 */
@Data
public class SkuStockLockDTO {

    @NotNull(message = "产品ID不能为空")
    @ApiModelProperty(value = "产品ID",required=true)
    private Long prodId;

    @NotNull(message = "skuId不能为空")
    @ApiModelProperty(value = "skuId",required=true)
    private Long skuId;

    @NotNull(message = "orderId不能为空")
    @ApiModelProperty(value = "orderId",required=true)
    private String orderNumber;

    @NotNull(message = "商品数量不能为空")
    @Min(value = 1,message = "商品数量不能为空")
    @ApiModelProperty(value = "商品数量",required=true)
    private Integer count;

    public SkuStockLockDTO() {
    }

    public SkuStockLockDTO(Long prodId, Long skuId, String orderNumber, Integer count) {
        this.prodId = prodId;
        this.skuId = skuId;
        this.orderNumber = orderNumber;
        this.count = count;
    }


}
