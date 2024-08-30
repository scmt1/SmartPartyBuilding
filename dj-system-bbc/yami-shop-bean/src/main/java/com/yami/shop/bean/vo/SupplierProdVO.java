/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.vo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author Citrus
 */
@Data
public class SupplierProdVO {

    @ApiModelProperty(value = "供应商商品id")
    private Long supplierProdId;

    @ApiModelProperty(value = "供应商id")
    private Long supplierId;

    @ApiModelProperty(value = "商品id")
    private Long prodId;

    @ApiModelProperty(value = "商品名称")
    private String prodName;

    @ApiModelProperty(value = "skuId")
    private Long skuId;

    @ApiModelProperty(value = "sku名称")
    private String skuName;

    @ApiModelProperty(value = "最小订货量")
    private Integer minOrderQuantity;

    @ApiModelProperty(value = "采购价")
    private Double purchasePrice;

    @ApiModelProperty(value = "商品编码")
    private String partyCode;

    @ApiModelProperty(value = "sku图片")
    private String pic;

    @ApiModelProperty(value = "sku库存")
    private Integer stocks;

    @ApiModelProperty(value = "sku列表")
    private List<SupplierSkuVO> skuList;

    @ApiModelProperty(value = "商品状态")
    private Integer status;

}
