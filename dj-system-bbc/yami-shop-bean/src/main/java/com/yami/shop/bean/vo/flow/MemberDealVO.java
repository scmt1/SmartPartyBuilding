/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.vo.flow;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author
 */
@Data
public class MemberDealVO {

    @ApiModelProperty("成交会员数")
    private Integer payMemberNum;

    @ApiModelProperty("成交会员数占比")
    private Double payMemberNumRate;

    @ApiModelProperty("支付订单数")
    private Integer payOrderNum;

    @ApiModelProperty("客单价")
    private Double pricePerMember;

    @ApiModelProperty("支付金额")
    private Double payAmount;

    @ApiModelProperty("支付金额占比")
    private Double payAmountRate;
}
