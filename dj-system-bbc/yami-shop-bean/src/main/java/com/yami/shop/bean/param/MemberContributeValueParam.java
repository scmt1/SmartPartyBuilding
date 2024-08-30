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

/**
 * @author Yami
 */
@Data
public class MemberContributeValueParam {

    @ApiModelProperty("累积会员数")
    private Integer totalMember = 0;
    @ApiModelProperty("累积会员数占比")
    private Double totalMemberRate = 0.0;

    @ApiModelProperty("成交会员数")
    private Integer payMemberNum = 0;
    @ApiModelProperty("成交会员数占比")
    private Double payMemberNumRate = 0.0;

    @ApiModelProperty("支付订单数")
    private Integer payOrderNum = 0;

    @ApiModelProperty("支付金额")
    private Double payAmount = 0.0;
    @ApiModelProperty("支付金额占比")
    private Double payAmountRate = 0.0;

    @ApiModelProperty("客单价")
    private Double pricePerMember = 0.0;

    @ApiModelProperty("人均消费频次")
    private Double frequencyOfConsume = 0.0;




}
