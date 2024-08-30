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

import com.baomidou.mybatisplus.annotation.TableField;
import com.yami.shop.user.common.model.Coupon;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 余额充值级别表
 *
 * @author YXF
 * @date 2020-09-08 10:42:39
 */
@Data
public class UserBalanceDto{

    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long balanceId;
    /**
     * 充值余额标题
     */
    @ApiModelProperty(value = "充值余额标题")
    private String balanceTitle;
    /**
     * 充值金额
     */
    @ApiModelProperty(value = "充值金额")
    private Double rechargeAmount;
    /**
     * 背景图片
     */
    @ApiModelProperty(value = "背景图片")
    private String img;
    /**
     * 赠送金额
     */
    @ApiModelProperty(value = "赠送金额")
    private Double presAmount;
    /**
     * 赠送积分
     */
    @ApiModelProperty(value = "赠送积分")
    private Integer presScore;
    /**
     * 赠送成长值
     */
    @ApiModelProperty(value = "赠送成长值")
    private Integer presGrowth;

    @TableField(exist = false)
    @ApiModelProperty(value = "赠送优惠券列表")
    private List<Coupon> couponList;

}
