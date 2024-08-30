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

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Citrus
 * @date 2021/11/10 10:53
 */
@Data
public class ComboVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "套餐id")
    private Long comboId;

    @ApiModelProperty(value = "套餐名称")
    private String name;

    @ApiModelProperty(value = "状态， -1：已删除 0：已失效 1：开启")
    private Integer status;

    @ApiModelProperty(value = "商品数量")
    private Integer prodCount;

    @ApiModelProperty(value = "主商品")
    private ComboProdVO mainProd;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "套餐金额")
    private Double comboAmount;

    @ApiModelProperty(value = "搭配商品列表")
    private List<ComboProdVO> matchingProds;

    @ApiModelProperty(value = "搭配商品id列表")
    private List<Long> matchingProdIds;
}
