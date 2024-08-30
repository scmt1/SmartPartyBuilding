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

import java.util.List;

/**
 * @author Yami
 */
@Data
public class AreaDto{

    @ApiModelProperty(value = "地区id")
    private Long areaId;

    @ApiModelProperty(value = "地区名称")
    private String areaName;

    @ApiModelProperty(value = "地区上级id")
    private Long parentId;

    @ApiModelProperty(value = "地区层级")
    private Integer level;

    @ApiModelProperty(value = "是否选择")
    private Integer check;

    @ApiModelProperty(value = "下级地址集合")
    private List<AreaDto> areas;

    @ApiModelProperty(value = "下级地址的areaId")
    private List<Long> areaIds;
}
