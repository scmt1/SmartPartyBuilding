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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Citrus
 * @date 2021/11/12 9:23
 */
@Data
@ApiModel("赠品vo")
public class GiveawayVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "赠品id")
    private Long giveawayId;

    @ApiModelProperty(value = "赠品名称")
    private String name;

    @ApiModelProperty(value = "主商品id")
    private Long prodId;

    @ApiModelProperty(value = "状态 -1：已删除 0：关闭 1:开启")
    private Integer status;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "购买数量（购买了多少件才赠送赠品）")
    private Integer buyNum;

    @ApiModelProperty(value = "赠送商品列表")
    private List<GiveawayProdVO> giveawayProds;
}
