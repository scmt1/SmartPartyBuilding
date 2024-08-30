/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author lth
 * @Date 2021/9/23 16:10
 */
@Data
public class SkuExportParam {

    @ApiModelProperty("商品编码")
    private String partyCode;

    @ApiModelProperty("商品名称")
    private String prodName;

    @ApiModelProperty("规格")
    private String skuName;

    @ApiModelProperty("商品库存")
    private Integer stocks;

    @ApiModelProperty("商品价格")
    private BigDecimal price;
}
