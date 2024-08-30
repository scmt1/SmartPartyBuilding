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
public class MemberContributeValueVO {

    public MemberContributeValueVO(){
        this.payOrderNum = 0;
        this.totalMember = 0;
        this.totalMemberRate = 0.00;
        this.payMemberNum = 0;
        this.payMemberNumRate = 0.00;
        this.payAmount = 0.00;
        this.payAmountRate = 0.00;
        this.pricePerMember = 0.00;
        this.frequencyOfConsume = 0.00;
    }
    @ApiModelProperty("累积会员数")
    private Integer totalMember;
    @ApiModelProperty("累积会员数占比")
    private Double totalMemberRate;

    @ApiModelProperty("成交会员数")
    private Integer payMemberNum;
    @ApiModelProperty("成交会员数占比")
    private Double payMemberNumRate;

    @ApiModelProperty("支付订单数")
    private Integer payOrderNum;

    @ApiModelProperty("支付金额")
    private Double payAmount;
    @ApiModelProperty("支付金额占比")
    private Double payAmountRate;

    @ApiModelProperty("客单价")
    private Double pricePerMember;

    @ApiModelProperty("人均消费频次")
    private Double frequencyOfConsume;
}
