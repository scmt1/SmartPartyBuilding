/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @author Yami
 */
@Data
@Accessors(chain = true)
public class OrderPayParam {

    @ApiModelProperty(value = "支付金额")
    private Double payActualTotal;

    @ApiModelProperty(value = "昨天24小时支付金额")
    private List<Double> payYesterdayActualTotal;

    @ApiModelProperty(value = "今日支付金额")
    private List<Double> nowActualTotal;

    @ApiModelProperty(value = "支付客户数")
    private Integer payUserCount;

    @ApiModelProperty(value = "较前一日支付客户数变化比率")
    private Double yesterdayPayUserRate;

    @ApiModelProperty(value = "客单价")
    private Double onePrice;

    @ApiModelProperty(value = "较前一日客单价变化比率")
    private Double yesterdayOnePriceRate;

    @ApiModelProperty(value = "成功退款金额")
    private Double refund;

    @ApiModelProperty(value = "较前一日退款金额变化比率")
    private Double yesterdayRefundRate;

    @ApiModelProperty(value = "支付订单数")
    private Integer payOrderCount;

    @ApiModelProperty(value = "较前一日支付订单数变化比率")
    private Double yesterdayPayOrderRate;

    @ApiModelProperty(value = "支付时间hour")
    private String Dates;

    @ApiModelProperty(value = "支付天数")
    private Date payDay;
}
