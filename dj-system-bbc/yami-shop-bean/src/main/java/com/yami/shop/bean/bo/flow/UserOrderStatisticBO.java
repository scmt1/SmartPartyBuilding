package com.yami.shop.bean.bo.flow;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 用户相关订单统计数据
 * @author: cl
 * @date: 2021-04-14 14:04:01
 */
@Data
public class UserOrderStatisticBO {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("最近消费时间")
    private Date reConsTime;

    @ApiModelProperty("消费金额")
    private Double consAmount;

    @ApiModelProperty("实付金额")
    private Double actualAmount;

    @ApiModelProperty("消费次数")
    private Integer consTimes;

    @ApiModelProperty("平均折扣")
    private Double averDiscount;

    @ApiModelProperty("售后金额")
    private Double afterSaleAmount;

    @ApiModelProperty("售后次数")
    private Integer afterSaleTimes;
}
