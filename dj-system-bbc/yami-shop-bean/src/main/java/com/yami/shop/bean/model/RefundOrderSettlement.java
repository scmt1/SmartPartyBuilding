/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author LGH
 * @date 2019-08-21 15:57:40
 */
@Data
@TableName("tz_refund_order_settlement")
public class RefundOrderSettlement implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId
    @ApiModelProperty("退款结算单据id")
    private Long settlementId;

    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty("订单支付单号")
    private String orderPayNo;

    @ApiModelProperty("退款单编号")
    private String refundSn;

    @ApiModelProperty("微信/支付宝退款单号（支付平台退款单号）")
    private String payRefundId;

    @ApiModelProperty("支付方式(1:微信支付 2支付宝支付)")
    private Integer payType;

    @ApiModelProperty("退款金额")
    private Double refundAmount;

    @ApiModelProperty("订单总额")
    private Double orderTotalAmount;

    @ApiModelProperty("用户ID")
    private String userId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @Version
    @ApiModelProperty("版本号")
    private Integer version;

    @ApiModelProperty("退款状态(1:申请中 2：已完成 -1失败)")
    private Integer refundStatus;
}
