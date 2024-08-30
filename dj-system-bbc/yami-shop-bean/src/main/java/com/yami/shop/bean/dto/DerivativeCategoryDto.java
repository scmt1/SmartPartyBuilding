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

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Yami
 */
@Data
public class DerivativeCategoryDto implements Serializable {

    @ApiModelProperty(value = "类目ID", required=true)
    private Long categoryId;

    @ApiModelProperty(value = "店铺id", required=true)
    private Long shopId;

    @ApiModelProperty(value = "父节点", required=true)
    private Long parentId = 0L;

    @ApiModelProperty(value = "产品类目名称", required=true)
    private String categoryName;

    @ApiModelProperty(value = "产品类目名称 英文")
    @TableField(exist = false)
    private String categoryNameEn;

    @ApiModelProperty(value = "类目图标")
    private String icon;

    @ApiModelProperty(value = "类目的显示图片")
    private String pic;

    @ApiModelProperty(value = "排序", required=true)
    private Integer seq;

    @ApiModelProperty(value = "分类扣率")
    private Double deductionRate;

    @ApiModelProperty(value = "默认是1，表示正常状态,0为下线状态", required=true)
    private Integer status;

    @ApiModelProperty(value = "记录时间", required=true)
    private Date recTime;

    @ApiModelProperty(value = "分类层级", required=true)
    private Integer grade;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

}
