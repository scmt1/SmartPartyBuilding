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

/**
 * @Author lth
 * @Date 2021/7/21 16:25
 */
@Data
@TableName("tz_category_shop")
public class CategoryShop {
    /**
     * 主键
     */
    @TableId
    private Long categoryShopId;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @ApiModelProperty(value = "分类id", required=true)
    private Long categoryId;

    @ApiModelProperty(value = "扣率: 为空代表采用平台扣率（1代表1%）")
    private Double rate;

    @ApiModelProperty(value = "授权资质图片，以,分割")
    private String qualifications;

    @ApiModelProperty(value = "签约状态：1：已通过 0待审核 -1未通过")
    private Integer status;
}
