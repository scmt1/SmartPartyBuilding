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

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 订单支付记录
 * @author Citrus
 * @date 2022-05-18 17:03:18
 */
@Data
@TableName("tz_pay_info")
@ApiModel("订单支付记录")
public class PayInfo implements Serializable{

    @ApiModelProperty(value = "支付单号")
    private String payNo;

    @ApiModelProperty(value = "外部订单流水号")
    private String bizPayNo;

    /**
      * 第三方系统的订单号
      * (paypal支付有个回调时间，在回调之前，目前还没有看到可以用本地系统交易单号来查询交易单，
      * 只有通过记录第三方系统的订单号，来查询第三方系统的订单
      * )
     */
    @ApiModelProperty(value = "第三方系统的订单号(paypal)")
    private String bizOrderNo;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "本次支付关联的多个订单号")
    private String orderNumbers;

    @ApiModelProperty(value = "支付入口 0订单 1充值 2会员")
    private Integer payEntry;

    @ApiModelProperty(value = "支付方式 1微信 2支付宝")
    private Integer payType;

    @ApiModelProperty(value = "支付状态")
    private Integer payStatus;

    @ApiModelProperty(value = "支付积分")
    private Long payScore;

    @ApiModelProperty(value = "支付金额")
    private Double payAmount;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "版本号")
    private Integer version;

    @ApiModelProperty(value = "回调内容")
    private String callbackContent;

    @ApiModelProperty(value = "回调时间")
    private Date callbackTime;
}
