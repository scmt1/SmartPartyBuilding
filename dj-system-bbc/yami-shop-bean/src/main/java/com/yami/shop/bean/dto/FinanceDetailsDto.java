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

import java.util.Date;

/**
 * 财务—财务明细
 * @author SJL
 * @date 2020-08-17
 */
@Data
public class FinanceDetailsDto {

    @ApiModelProperty("交易时间")
    private Date transDate;

    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty("下单时间")
    private Date placeTime;

    @ApiModelProperty("交易摘要")
    private String prodName;

    @ApiModelProperty("发生渠道")
    private String shopName;

    @ApiModelProperty("资金变更申请人")
    private String userName;

    @ApiModelProperty("支付平台交易单号")
    private String bizPayNo;

    @ApiModelProperty("业务类型 1.订单支付 2.订单退款 3.余额充值 4.购买会员")
    private Integer bizType;

    /**
     * @see com.yami.shop.common.enums.PayType
     */
    @ApiModelProperty("支付方式 -1.不限")
    private Integer payType;

    @ApiModelProperty("收支金额")
    private Double transAmount;
}
