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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 品牌店铺关联信息VO
 *
 * @author FrozenWatermelon
 * @date 2021-05-08 13:31:45
 */
@Data
public class BrandShopVO {

    @ApiModelProperty("")
    private Long brandShopId;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("品牌id")
    private Long brandId;

    @ApiModelProperty("授权资质图片，以,分割")
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String qualifications;

    @ApiModelProperty("类型 0：平台品牌，1：店铺自定义品牌")
    private Integer type;

    @ApiModelProperty("索引首字母")
    private String firstLetter;

    @ApiModelProperty("logo")
    private String imgUrl;

    @ApiModelProperty("品牌名称")
    private String name;

    @ApiModelProperty("品牌状态")
    private Integer brandStatus;

}
