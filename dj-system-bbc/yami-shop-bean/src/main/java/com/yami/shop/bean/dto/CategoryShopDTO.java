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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author lth
 * @Date 2021/7/28 10:23
 */
@Data
public class CategoryShopDTO {
    @ApiModelProperty("主键id")
    private Long categoryShopId;

    @ApiModelProperty("分类id")
    private Long categoryId;

    @ApiModelProperty("分类名称")
    private String name;

    @ApiModelProperty("上级分类名称")
    private String parentName;

    @ApiModelProperty(value = "平台一级分类id")
    private Long primaryCategoryId;

    @ApiModelProperty(value = "平台二级分类id")
    private Long secondaryCategoryId;

    @ApiModelProperty("平台扣率")
    private Double platformRate;

    @ApiModelProperty("自定义扣率，为空代表采用平台扣率")
    private Double customizeRate;

    @ApiModelProperty("经营资质")
    @JsonSerialize(using = ImgJsonSerializer.class)
    private String qualifications;

    @ApiModelProperty("签约状态：1：已通过 0待审核 -1未通过")
    private Integer status;

    @ApiModelProperty("分类状态 1:上架 0:下架 -1:已删除")
    private Integer categoryStatus;

    @ApiModelProperty("店铺id")
    private Long shopId;
}
