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
 * @author lhd
 * @date 2021/8/10 16:00
 */
@Data
public class ShopAccountDetailVO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("店铺id 0是平台")
    private Long shopId;

    @ApiModelProperty("店铺名称 平台是官方店")
    private String shopName;

    @ApiModelProperty("支付时间")
    private Date payTime;

    @ApiModelProperty("业务类型，当查询平台时用到 1.订单 2.余额充值 3.购买会员")
    private Integer bizType;

    @ApiModelProperty("订单编号")
    private String orderNumber;

    @ApiModelProperty("支付单号")
    private String payNo;

    @ApiModelProperty("外部流水号")
    private String bizPayNo;

    @ApiModelProperty("支付方式")
    private Integer payType;

    @ApiModelProperty("支付宝金额")
    private Double alipayAmount;

    @ApiModelProperty("微信金额")
    private Double wechatAmount;

    @ApiModelProperty("余额金额")
    private Double balanceAmount;

    @ApiModelProperty("paypal金额")
    private Double paypalAmount;

    @ApiModelProperty("积分数目")
    private Long scoreCount;

    @ApiModelProperty("合计")
    private Double total;
}
