package com.yami.shop.bean.vo.flow;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author lhd
 */
@Data
public class MemberOverviewVO {

    public MemberOverviewVO(){
        this.totalMember = 0;
        this.totalMemberRate = 0.0;
        this.newMember = 0;
        this.newMemberRate = 0.0;
        this.payMember = 0;
        this.payMemberRate = 0.0;
        this.couponMember = 0;
        this.couponMemberRate = 0.0;
        this.memberPayAmount = 0.0;
        this.memberPayAmountRate = 0.0;
        this.memberPayOrder = 0;
        this.memberPayOrderRate = 0.0;
        this.memberSingleAmount = 0.0;
        this.memberSingleAmountRate = 0.0;
    }
    private Long currentDay;

    @ApiModelProperty("累积会员数")
    private Integer totalMember;

    @ApiModelProperty("新增会员数")
    private Integer newMember;

    @ApiModelProperty("付费会员数")
    private Integer paidMember;

    @ApiModelProperty("支付会员数")
    private Integer payMember;

    @ApiModelProperty("领券会员数")
    private Integer couponMember;

    @ApiModelProperty("会员支付金额")
    private Double memberPayAmount;

    @ApiModelProperty("会员支付订单数")
    private Integer memberPayOrder;

    @ApiModelProperty("会员客单价")
    private Double memberSingleAmount;

    @ApiModelProperty("累积会员与之前的  >0 上升/ <0下降 率,")
    private Double totalMemberRate;

    @ApiModelProperty("新增会员会员与之前的 上升/下降 率")
    private Double newMemberRate;

    @ApiModelProperty("支付会员数，变化率")
    private Double payMemberRate;

    @ApiModelProperty("领券会员数，变化率")
    private Double couponMemberRate;

    @ApiModelProperty("会员支付金额，变化率")
    private Double memberPayAmountRate;

    @ApiModelProperty("会员支付订单数, 变化率")
    private Double memberPayOrderRate;

    @ApiModelProperty("会员客单价, 变化率")
    private Double memberSingleAmountRate;

    @ApiModelProperty("符合条件的userIds")
    private List<Long> userIds;

    @ApiModelProperty("储值会员数")
    private Integer storedValue;
}
