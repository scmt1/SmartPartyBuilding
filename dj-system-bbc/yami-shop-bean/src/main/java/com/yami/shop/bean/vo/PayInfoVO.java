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

/**
 * @author Pineapple
 * @date 2021/6/10 9:13
 */
@Data
public class PayInfoVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("序号")
    private Long index;

    @ApiModelProperty("外部订单流水号")
    private String bizPayNo;

    @ApiModelProperty("关联订单号")
    private String orderIds;

    @ApiModelProperty("支付入口[0订单 1充值 2开通会员]")
    private Integer payEntry;

    /**
     * 支付方式 [0积分支付    1微信小程序支付    2支付宝支付    3微信扫码支付
     * 4微信H5支付    5微信公众号支付    6支付宝H5支付    7支付宝APP支付    8微信APP支付    9余额支付]
     */
    @ApiModelProperty("支付方式")
    private Integer payType;

    @ApiModelProperty("支付状态[-1退款 0未支付 1已支付]")
    private Integer payStatus;

    @ApiModelProperty("支付积分")
    private Long payScore;

    @ApiModelProperty("支付金额")
    private Long payAmount;

    @ApiModelProperty("回调时间")
    private Date callbackTime;

}
