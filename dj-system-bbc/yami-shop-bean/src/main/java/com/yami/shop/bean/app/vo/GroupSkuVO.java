/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.app.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author yami
 * @date 2019/8/30 10:25
 */
@Data
public class GroupSkuVO {

    @ApiModelProperty("拼团活动商品规格id")
    private Long groupSkuId;

    @ApiModelProperty(value = "商品规格Id")
    private Long skuId;

    @ApiModelProperty(value = "原售价格")
    private Double price;

    @ApiModelProperty(value = "活动价格")
    private Double actPrice;

    @ApiModelProperty(value = "sku名称")
    private String skuName;

    @ApiModelProperty(value = "库存")
    private Integer stocks;

    @JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty(value = "sku图片")
    private String pic;

    @ApiModelProperty(value = "销售属性组合字符串 格式是p1:v1;p2:v2")
    private String properties;
}
