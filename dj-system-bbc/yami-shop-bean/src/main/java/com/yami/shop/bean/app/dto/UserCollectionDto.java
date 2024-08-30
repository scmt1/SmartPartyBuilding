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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author Yami
 */
@ApiModel("收藏对象")
@Data
public class UserCollectionDto {

    @ApiModelProperty(value = "收藏id")
    private Long id;

    @ApiModelProperty(value = "商品id")
    private Long prodId;

    @ApiModelProperty(value = "商品名称")
    private String prodName;

    @ApiModelProperty(value = "收藏时间")
    private Date createTime;

    @ApiModelProperty(value = "商品现价")
    private Double price;

    @ApiModelProperty(value = "商品原价")
    private Double oriPrice;

    @JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty(value = "商品主图")
    private String pic;

    @ApiModelProperty(value = "-1:删除、0:商家下架、1:上架、2:违规下架、3:平台审核")
    private Integer status;

}
