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
 * 
 *
 * @author Citrus
 * @date 2022-05-25 15:45:45
 */
@Data
@TableName("tz_refund_info")
@ApiModel("")
public class RefundInfo implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("退款单号")
    private String refundId;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    @ApiModelProperty(value = "关联的支付订单id")
    private String orderNumber;
    @ApiModelProperty(value = "关联的支付单id")
    private String payNo;
    @ApiModelProperty(value = "微信支付宝关联的退款id")
    private String payRefundId;
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "退款状态")
    private Integer refundStatus;
    @ApiModelProperty(value = "退款金额")
    private Double refundAmount;
    @ApiModelProperty(value = "支付方式")
    private Integer payType;
    @ApiModelProperty(value = "回调内容")
    private String callbackContent;
    @ApiModelProperty(value = "回调时间")
    private Date callbackTime;
}
