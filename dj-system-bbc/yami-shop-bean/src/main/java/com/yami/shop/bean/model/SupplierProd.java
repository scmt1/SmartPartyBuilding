/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Citrus
 * @date 2021-09-07 10:12:32
 */
@Data
@TableName("tz_supplier_prod")
public class SupplierProd implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty(value = "供应商商品id")
    private Long supplierProdId;

    @NotNull(message = "供应商id不能为空")
    @ApiModelProperty(value = "供应商id")
    private Long supplierId;

    @ApiModelProperty(value = "商品id")
    @NotNull(message = "商品id不能为空")
    private Long prodId;

    @ApiModelProperty(value = "skuId")
    private Long skuId;

    @ApiModelProperty(value = "最小订货量")
    @NotNull(message = "最小订货量不能为空")
    private Integer minOrderQuantity;

    @ApiModelProperty(value = "采购价")
    @NotNull(message = "采购价不能为空")
    private Double purchasePrice;
}
