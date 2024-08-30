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
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 财务—财务明细
 * @author SJL
 * @date 2020-08-18
 */
@Data
public class FinanceDetailsParam {

    @ApiModelProperty("开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    @ApiModelProperty("订单号")
    private String orderNumber;

    /**
     * @see com.yami.shop.common.enums.PayType
     */
    @ApiModelProperty("支付方式 -1不限 0积分 1微信 2支付宝...")
    @NotNull(message = "支付方式不能为空")
    private Integer payType;

    @ApiModelProperty("语言")
    private Integer lang;
}
