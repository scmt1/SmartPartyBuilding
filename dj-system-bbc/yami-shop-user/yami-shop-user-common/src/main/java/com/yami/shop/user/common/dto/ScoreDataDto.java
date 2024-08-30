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

import java.io.Serializable;
import java.util.List;

/**
 * 积分中心信息
 *
 * @author LHD
 * @date 2019-12-19 10:27:46
 *
 * @author yami
 */
@Data
public class ScoreDataDto implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 签到积分列表
     */
    @ApiModelProperty(value = "签到积分列表")
    private List<Integer> scoreList;
    /**
     * 用户积分
     */
    @ApiModelProperty(value = "用户积分")
    private Long score;
    /**
     * 上次结算过期的积分
     */
    @ApiModelProperty(value = "上次结算过期的积分")
    private Long expireScore;
    /**
     * 过期时间(年)
     */
    @ApiModelProperty(value = "过期时间(年)")
    private Integer expireYear;
    /**
     * 积分过期开关
     */
    @ApiModelProperty(value = "积分过期开关")
    private Boolean scoreExpireSwitch;
    /**
     * 等级名称
     */
    @ApiModelProperty(value = "等级名称")
    private String levelName;
    /**
     * 等级类型 0 普通会员 1 付费会员
     */
    @ApiModelProperty(value = "等级类型 0 普通会员 1 付费会员")
    private Integer levelType;
    /**
     * 用户当前成长值
     */
    @ApiModelProperty(value = "用户当前成长值")
    private Integer growth;
    /**
     * 注册可获取积分
     */
    @ApiModelProperty(value = "注册可获取积分")
    private Long registerScore;
    /**
     * 购物可获取积分
     */
    @ApiModelProperty(value = "购物可获取积分")
    private Double shopScore;

    /**
     * 是否已经签到 1是 0否
     */
    @ApiModelProperty(value = "是否已经签到 1是 0否")
    private Integer isSignIn;

    /**
     * 是否已经注册 1是 0否
     */
    @ApiModelProperty(value = "是否已经注册 1是 0否")
    private Integer isRegister;

    /**
     * 签到第几天
     */
    @ApiModelProperty(value = "签到第几天")
    private Integer signInCount;

}
