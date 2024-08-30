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
public class MemberDealTreadVO {

    private Long currentDay;

    @ApiModelProperty("新成交会员数")
    private Integer newPayMemberNum;
    @ApiModelProperty("老成交会员数")
    private Integer oldPayMemberNum;

    @ApiModelProperty("新支付订单数")
    private Integer newPayOrderNum;
    @ApiModelProperty("老支付订单数")
    private Integer oldPayOrderNum;

    @ApiModelProperty("新客单价")
    private Double newPricePerMember;
    @ApiModelProperty("老客单价")
    private Double oldPricePerMember;

    @ApiModelProperty("新支付金额")
    private Double newPayAmount;
    @ApiModelProperty("老支付金额")
    private Double oldPayAmount;
}
