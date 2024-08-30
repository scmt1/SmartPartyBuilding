/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.vo.search;

import com.yami.shop.bean.bo.CategoryBO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 商品信息
 *
 * @author YXF
 */
@Data
public class ProductSearchVO {

    @ApiModelProperty(value = "商品id")
    private Long prodId;

    @ApiModelProperty(value = "商品名称")
    private String prodName;

    @ApiModelProperty(value = "卖点")
    private String brief;

    @ApiModelProperty(value = "商品售价")
    private Double price;

    @ApiModelProperty(value = "市场价")
    private Double oriPrice;

    @ApiModelProperty(value = "活动价(秒杀、团购活动的商品价格)")
    private Double activityPrice;

    @ApiModelProperty(value = "活动商品原价")
    private Double activityOriginalPrice;

    @ApiModelProperty(value = "积分价")
    private Double scorePrice;

    @ApiModelProperty(value = "商品介绍主图")
    private String pic;

    @ApiModelProperty(value = "商品介绍图")
    private String imgs;

    @ApiModelProperty(value = "商品类型(0普通商品 1拼团 2秒杀 3积分)")
    private Integer prodType;

    @ApiModelProperty(value = "商品类别 0.实物商品 1. 虚拟商品")
    private Integer mold;

    @ApiModelProperty(value = "预售状态 1：开启 0：未开启")
    private Integer preSellStatus;

    @ApiModelProperty(value = "店铺名称 搜索华为的时候，可以把华为的旗舰店搜索出来")
    private String shopName;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "店铺logo")
    private String shopImg;

    @ApiModelProperty(value = "店铺类型1自营店")
    private Integer shopType;

    @ApiModelProperty(value = "商品状态")
    private Integer status;

    @ApiModelProperty(value = "库存")
    private Integer totalStocks;

    @ApiModelProperty(value = "销量")
    private Integer soldNum;

    @ApiModelProperty(value = "注水销量")
    private Integer waterSoldNum;

    @ApiModelProperty(value = "评论数")
    private Integer commentNum;

    @ApiModelProperty(value = "好评率")
    private Double positiveRating;

    @ApiModelProperty(value = "配送方式")
    private String deliveryMode;

    @ApiModelProperty(value = "商品创建时间")
    private Long createTime;

    @ApiModelProperty(value = "商品更新时间")
    private Long updateTime;

    @ApiModelProperty(value = "商品序号")
    private Integer seq;

    @ApiModelProperty(value = "是否置顶")
    private Integer isTop;

    @ApiModelProperty(value = "活动id")
    private Long activityId;

    @ApiModelProperty(value = "平台一级分类id")
    private Long primaryCategoryId;

    @ApiModelProperty(value = "平台二级分类id")
    private Long secondaryCategoryId;

    @ApiModelProperty(value = "平台三级分类信息")
    private CategoryBO category;

    @ApiModelProperty(value = "品牌id")
    private Long brandId;

    @ApiModelProperty(value = "平台分类id")
    private Long categoryId;

    @ApiModelProperty(value = "店铺分类id")
    private Long shopCategoryId;

    @ApiModelProperty(value = "活动开始时间")
    private Long activityStartTime;

    @ApiModelProperty(value = "活动进行中")
    private Boolean activityInProgress;

    @ApiModelProperty(value = "商品团购信息")
    private GroupActivitySearchVO groupActivitySearchVO;

    @ApiModelProperty(value = "商品秒杀信息")
    private SeckillSearchVO seckillSearchVO;


}
