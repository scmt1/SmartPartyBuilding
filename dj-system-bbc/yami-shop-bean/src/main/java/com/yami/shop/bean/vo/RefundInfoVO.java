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
 * 退款信息VO
 *
 * @author FrozenWatermelon
 * @date 2021-03-15 15:26:03
 */
@Data
public class RefundInfoVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("序号")
    private Long index;

    @ApiModelProperty("退款单号")
    private Long refundId;

    @ApiModelProperty("关联的支付订单id")
    private Long orderId;

    @ApiModelProperty("关联的支付单id")
    private Long payId;

    @ApiModelProperty("退款状态")
    private Integer refundStatus;

    @ApiModelProperty("退款金额")
    private Long refundAmount;

    @ApiModelProperty("支付方式")
    private Integer payType;

    @ApiModelProperty("回调内容")
    private String callbackContent;

    @ApiModelProperty("回调时间")
    private Date callbackTime;

}
