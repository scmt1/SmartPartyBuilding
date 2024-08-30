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

import java.io.Serializable;
import java.util.Date;

/**
 * @author Yami
 */
@Data
@TableName("tz_basket")
public class Basket implements Serializable {

    @TableId
    @ApiModelProperty("购物车id")
    private Long basketId;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "商品id")
    private Long prodId;

    @ApiModelProperty(value = "skuId")
    private Long skuId;

    @ApiModelProperty(value = "用户ID")
    private String userId;

    @ApiModelProperty(value = "购物车产品个数")
    private Integer basketCount;

    @ApiModelProperty(value = "购物时间")
    private Date basketDate;

    @ApiModelProperty(value = "满减活动ID")
    private Long discountId;

    @ApiModelProperty(value = "分销推广人卡号")
    private String distributionCardNo;

    @ApiModelProperty(value = "套餐id")
    private Long comboId;

    @ApiModelProperty(value = "套餐数量")
    private Integer comboCount;

    @ApiModelProperty(value = "主购物车id（套餐）")
    private Long parentBasketId;

    @ApiModelProperty(value = "商品是否勾选 true：勾选 ")
    private Boolean isChecked;
}
