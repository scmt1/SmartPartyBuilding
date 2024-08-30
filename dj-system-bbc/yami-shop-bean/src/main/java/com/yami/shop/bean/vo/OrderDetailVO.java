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

import java.util.List;

/**
 * @author lhd
 */
@ApiModel("订单详细信息")
@Data
public class OrderDetailVO {

    @ApiModelProperty(value = "店铺名称",required=true)
    private String shopName;

    @ApiModelProperty(value = "订单运费",required=true)
    private Double freightAmount;

    @ApiModelProperty(value = "店铺运费减免金额",required=true)
    private Double freeFreightAmount;

    @ApiModelProperty(value = "平台运费减免金额",required=true)
    private Double platformFreeFreightAmount;

    @ApiModelProperty(value = "订单项详细信息")
    private List<OrderItemDetailVO> orderItemDetailList;
}
