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

/**
 * @Author lth
 * @Date 2021/7/21 16:25
 */
@Data
@TableName("tz_brand_shop")
public class BrandShop implements Serializable {

    @TableId
    private Long brandShopId;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "品牌id")
    private Long brandId;

    @ApiModelProperty(value = "授权资质图片，以,分割")
    private String qualifications;

    @ApiModelProperty(value = "类型 0：平台品牌，1：店铺自定义品牌")
    private Integer type;

    @ApiModelProperty(value = "签约状态：1：已通过 0待审核 -1未通过")
    private Integer status;
}
