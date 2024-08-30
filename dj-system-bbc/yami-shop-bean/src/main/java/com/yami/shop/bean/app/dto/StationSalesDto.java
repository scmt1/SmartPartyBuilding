/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.app.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 自提点销售记录
 *
 * @author YXF
 * @date 2020-06-04 15:31:29
 */
@Data
public class StationSalesDto {

    @ApiModelProperty(value = "支付订单数", required = true)
    private int payOrderNumber;

    /**
     * 平台优惠金额
     */
    private double platformAmount;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 核销数量
     */
    private int writeOffNum;

    /**
     * 核销数量
     */
    private String userId;

    /**
     * 核销数量
     */
    private double productNums;

    /**
     * 实际支付金额
     */
    private double actualTotal;
    /**
     * 支付人数
     */
    private int userNum;

    /**
     *  支付金额 = 平台优惠金额 + 实际支付金额
     */
    @ApiModelProperty(value = "支付金额", required = true)
    private double payAmount;

    @ApiModelProperty(value = "客单价", required = true)
    private double customerUnitPrice;

    @ApiModelProperty(value = "1:单天营业额，2:该月营业额", required = true)
    private int salesType;
}
