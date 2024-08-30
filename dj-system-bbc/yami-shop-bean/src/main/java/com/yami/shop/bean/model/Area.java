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
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * @author Yami
 */
@Data
@TableName("tz_area")
public class Area implements Serializable {
    private static final long serialVersionUID = -6013320537436191451L;

    @TableId
    @ApiModelProperty(value = "地区id")
    private Long areaId;

    @ApiModelProperty(value = "地区名称")
    @Size(min = 1, max = 20, message = "地区名称长度不能超过20")
    @NotBlank(message = "地区名称不能为空")
    private String areaName;

    @ApiModelProperty(value = "地区上级id")
    @NotNull(message = "地区上级id不能为空")
    private Long parentId;
    /**
     * @see com.yami.shop.bean.enums.AreaLevelEnum
     */
    @ApiModelProperty(value = "地区层级")
    @NotNull(message = "地区层级不能为空")
    private Integer level;

    @TableField(exist=false)
    @ApiModelProperty(value = "下级地址集合")
    private List<Area> areas;

    @TableField(exist=false)
    @ApiModelProperty(value = "最大级别")
    private Integer maxGrade;
}
