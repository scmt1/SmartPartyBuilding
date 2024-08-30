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
import java.util.Date;
import java.util.List;

/**
 *
 * @author LHD
 * @date 2019-12-19 10:27:46
 */
@Data
public class LevelDto implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 用户昵称
     */
    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    /**
     * 用户当前等级
     */
    @ApiModelProperty(value = "用户当前会员等级名称")
    private String levelName;
    /**
     * 等级条件 0 普通会员 1 付费会员
     */
    @ApiModelProperty(value = "用户当前会员等级类型 0 普通会员 1 付费会员")
    private Integer levelType;
    /**
     * 用户年费会员过期时间
     */
    @ApiModelProperty(value = "用户会员过期时间")
    private Date endTime;

    /**
     * 用户当前等级
     */
    @ApiModelProperty(value = "用户当前等级")
    private LevelDetailDto userLevel;

    /**
     * 用户当前等级所需成长值
     */
    @ApiModelProperty(value = "当前等级所需成长值")
    private Integer needGrowth;

    /**
     * 用户当前成长值
     */
    @ApiModelProperty(value = "用户当前成长值")
    private Integer growth;

    /**
     * 用户当前积分
     */
    @ApiModelProperty(value = "用户当前积分")
    private Long score = 0L;

    /**
     * 等级列表
     */
    @ApiModelProperty(value = "等级列表")
    private List<LevelDetailDto> userLevels;


    /**
     * 下一等级名称
     */
    @ApiModelProperty(value = "下一等级名称")
    private String nextLevelName;

    /**
     * 下一等级所需成长值
     */
    @ApiModelProperty(value = "下一等级所需成长值")
    private Integer nextGrowth;


}
