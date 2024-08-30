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

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 套装商品项
 *
 * @author LGH
 * @date 2021-11-08 13:29:16
 */
@Data
@TableName("tz_giveaway_prod")
public class GiveawayProd implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty(value = "赠品商品项id")
    private Long giveawayProdId;

    @ApiModelProperty(value = "赠品id")
    private Long giveawayId;

    @ApiModelProperty(value = "商品id")
    @NotNull(message = "商品id不能为空")
    private Long prodId;

    @ApiModelProperty(value = "skuId")
    @NotNull(message = "skuId不能为空")
    private Long skuId;

    @ApiModelProperty(value = "赠送数量")
    @NotNull(message = "赠送数量不能为空")
    private Integer giveawayNum;

    @ApiModelProperty(value = "退货价")
    @NotNull(message = "退货价不能为空")
    private Double refundPrice;

    @ApiModelProperty(value = "状态 -1：删除 1：正常")
    private Integer status;

    @ApiModelProperty(value = "商品名称")
    @TableField(exist = false)
    private String prodName;

    @ApiModelProperty(value = "商品图片")
    @JsonSerialize(using = ImgJsonSerializer.class)
    @TableField(exist = false)
    private String pic;

    @ApiModelProperty(value = "sku名称")
    @TableField(exist = false)
    private String skuName;

    @ApiModelProperty(value = "商品现价")
    @TableField(exist = false)
    private Double price;

    @ApiModelProperty(value = "店铺id")
    @TableField(exist = false)
    private Long shopId;

    @ApiModelProperty(value = "店铺分类id")
    @TableField(exist = false)
    private Long shopCategoryId;
}
