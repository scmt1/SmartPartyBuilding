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
public class MemberOverviewParam {

    private Integer dataType;

    private Long currentDay;

    @ApiModelProperty("累积会员数")
    private Integer totalMember = 0;
    @ApiModelProperty("累积会员与之前的  >0 上升/ <0下降 率,")
    private Double totalMemberRate = 0.0;

    @ApiModelProperty("新增会员数")
    private Integer newMember = 0;
    @ApiModelProperty("新增会员会员与之前的 上升/下降 率")
    private Double newMemberRate = 0.0;

    @ApiModelProperty("支付会员数")
    private Integer payMember = 0;
    @ApiModelProperty("支付会员数，变化率")
    private Double payMemberRate = 0.0;

    @ApiModelProperty("领券会员数")
    private Integer couponMember = 0;
    @ApiModelProperty("领券会员数，变化率")
    private Double couponMemberRate = 0.0;

    @ApiModelProperty("会员支付金额")
    private Double memberPayAmount = 0.0;
    @ApiModelProperty("会员支付金额，变化率")
    private Double memberPayAmountRate = 0.0;

    @ApiModelProperty("会员支付订单数")
    private Integer memberPayOrder = 0;
    @ApiModelProperty("会员支付订单数, 变化率")
    private Double memberPayOrderRate = 0.0;

    @ApiModelProperty("会员客单价")
    private Double memberSingleAmount = 0.0;
    @ApiModelProperty("会员客单价, 变化率")
    private Double memberSingleAmountRate = 0.0;

}
