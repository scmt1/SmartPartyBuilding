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
 * @author Citrus
 */
@Data
public class SupplierVO {

    @ApiModelProperty(value = "供应商id")
    private Long supplierId;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "供应商分类id")
    private Long supplierCategoryId;

    @ApiModelProperty(value = "供应商名称")
    private String supplierName;

    @ApiModelProperty(value = "供应商分类名称")
    private String categoryName;

    @ApiModelProperty(value = "供应商商品数量")
    private Integer supplierProdCount;

    @ApiModelProperty(value = "是否为默认供应商")
    private Integer isDefault;
}
