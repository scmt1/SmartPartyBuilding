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

/**
 *
 *
 * @author Citrus
 * @date 2021-09-07 10:12:32
 */
@Data
public class SupplierSkuVO {
    /**
     * 供应商商品id
     */
    private Long supplierProdId;
    /**
     * skuid
     */
    private Long skuId;
    /**
     * sku名称
     */
    @ApiModelProperty(value = "sku名称")
    private String skuName;
    /**
     * 最小订货量
     */
    private Integer minOrderQuantity;
    /**
     * 采购价
     */
    private Double purchasePrice;

    /**
     * 库存
     */
    @ApiModelProperty(value = "库存", required = true)
    private Integer stocks;

    /**
     * 商家编码
     */
    @ApiModelProperty(value = "商家编码")
    private String partyCode;

    /**
     * sku图片
     */
    @ApiModelProperty(value = "sku图片")
    private String pic;

}
