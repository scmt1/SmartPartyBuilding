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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Yami
 */
@Data
public class VisitorTopParam {

    /**
     * 商品id
     */
    @ApiModelProperty("商品id")
    private Integer prodId;

    /**
     * 店铺名称
     */
    @ApiModelProperty("店铺名称")
    private String shopName;

    /**
     * 商品名称
     */
    @ApiModelProperty("商品名称")
    private String prodName;

    /**
     * 商品主图
     */
    @ApiModelProperty("商品主图")
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String pic;

    /**
     *     访客数
     */
    @ApiModelProperty("访客数")
    private Integer visitorNum;
    /**
     *     访问-支付转化率
     *     支付的人数/访问的人数
     */
    @ApiModelProperty("访问-支付转化率")
    private Double visitorToPayRate;
}
