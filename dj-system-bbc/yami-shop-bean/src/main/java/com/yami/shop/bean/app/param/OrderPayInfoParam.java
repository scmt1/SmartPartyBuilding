/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.app.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Yami
 */
@Data
@ApiModel(value= "pc订单支付后返回信息")
public class OrderPayInfoParam {


    @ApiModelProperty(value = "商品名称")
    private List<String> prodNameList;

    @ApiModelProperty(value = "收货人姓名")
    private String receiver;

    @ApiModelProperty(value = "收货地址")
    private String userAddr;

    @ApiModelProperty(value = "收货人手机号")
    private String mobile;

    @ApiModelProperty(value = "订单状态 1：为待支付 ")
    private Integer status;

    @ApiModelProperty(value = "订单过期时间")
    private Date endTime;

    @ApiModelProperty(value = "总共需要支付金额")
    private Double totalFee;

    @ApiModelProperty(value = "总共需要支付积分")
    private Integer totalScore;
}
