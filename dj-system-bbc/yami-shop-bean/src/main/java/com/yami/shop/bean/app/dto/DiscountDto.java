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
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Yami
 */
@Data
public class DiscountDto implements Serializable {

    @ApiModelProperty("满减满折优惠id")
    private Long discountId;

    @ApiModelProperty("店铺ID")
    private Long shopId;

    @ApiModelProperty("活动名称")
    private String discountName;

    @ApiModelProperty("店铺名称")
    private String shopName;

    @ApiModelProperty("店铺Logo")
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String shopLogo;

    @ApiModelProperty("手机端活动图片")
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String mobilePic;

    @ApiModelProperty("pc端活动列表图片")
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String pcPic;

    @ApiModelProperty("pc端活动背景图片")
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String pcBackgroundPic;

    @ApiModelProperty("优惠规则(0:满钱减钱 1:满件减钱 2:满钱打折 3:满件打折)")
    private Integer discountRule;

    @ApiModelProperty("减免类型(0:按满足最高层级减一次 1:每满一次减一次)")
    private Integer discountType;

    @ApiModelProperty("适用商品类型(0:通用券 1:商品券)")
    private Integer suitableProdType;

    @ApiModelProperty("最多减多少")
    private Double maxReduceAmount;

    @ApiModelProperty("开始时间")
    private Date startTime;

    @ApiModelProperty("结束时间")
    private Date endTime;

    private List<DiscountItemDto> discountItems;

}
