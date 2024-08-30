/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.common.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 余额充值记录
 *
 * @author YXF
 * @date 2020-09-09 17:38:30
 */
@Data
public class UserBalanceLogDto{
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "升级时间")
    private Date createTime;
    /**
     * 改变余额
     */
    @ApiModelProperty(value = "改变余额")
    private Double changeBalance;
    /**
     * 收支类型 0支出 1收入
     */
    @ApiModelProperty(value = "收支类型 0支出 1收入")
    private Integer ioType;

    /**
     * 1:充值 2:使用 3:赠送
     */
    @ApiModelProperty(value = "1:充值 2:赠送 3:支付 4:退款")
    private Integer type;

    @ApiModelProperty(value = "订单号列表")
    private String orderNumbers;
}
