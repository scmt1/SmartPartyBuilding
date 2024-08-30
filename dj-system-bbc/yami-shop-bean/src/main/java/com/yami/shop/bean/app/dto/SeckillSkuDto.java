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

/**
 * @author LGH
 */
@Data
public class SeckillSkuDto {

    @ApiModelProperty(value = "秒杀sku拥有的库存", required = true)
    private Integer seckillStocks;

    @ApiModelProperty(value = "sku价格", required = true)
    private Double price;

    @ApiModelProperty(value = "秒杀价格", required = true)
    private Double seckillPrice;
//
//    @ApiModelProperty(value = "商品名称", required = true)
//    private String prodName;

    @ApiModelProperty(value = "sku图片", required = true)
    private String pic;
    @ApiModelProperty(value = "销售属性组合字符串,格式是p1:v1;p2:v2", required = true)
    private String properties;

    /**
     * 秒杀活动单个skuid
     */
    @ApiModelProperty(value = "秒杀活动单个skuid", required = true)
    private Long seckillSkuId;
    /**
     * skuId
     */
    @ApiModelProperty(value = "skuId", required = true)
    private Long skuId;
//    /**
//     * 秒杀活动id
//     */
//    @ApiModelProperty(value = "秒杀活动id", required = true)
//    private Long seckillId;
}
