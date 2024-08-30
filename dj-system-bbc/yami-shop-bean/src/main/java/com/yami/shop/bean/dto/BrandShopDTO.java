/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

/**
 * 品牌店铺关联信息DTO
 *
 * @author FrozenWatermelon
 * @date 2021-05-08 13:31:45
 */
@Data
public class BrandShopDTO {

    @ApiModelProperty("主键id")
    private Long brandShopId;

    @ApiModelProperty("店铺id")
    private Long shopId;

    @ApiModelProperty("品牌id")
    private Long brandId;

    @ApiModelProperty("授权资质图片，以,分割")
    @NotBlank(message = "授权资质图片不能为空")
    private String qualifications;

    @ApiModelProperty("类型 0：平台品牌，1：店铺自定义品牌")
    private Integer type;

    @ApiModelProperty("检索首字母")
    @Length(max = 1, message = "检索首字母长度不能超过1")
    private String firstLetter;

    @ApiModelProperty("排序")
    private Integer seq;

    @ApiModelProperty("是否置顶 0：不置顶  1：置顶")
    private Integer isTop;

    @ApiModelProperty("logo")
    private String imgUrl;

    @ApiModelProperty("品牌名称")
    private String name;

    @ApiModelProperty("品牌描述")
    private String desc;

    @ApiModelProperty("品牌状态")
    private Integer status;

}
