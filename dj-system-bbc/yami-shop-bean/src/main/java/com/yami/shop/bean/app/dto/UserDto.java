/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.app.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author Yami
 */
@Data
public class UserDto {


    @ApiModelProperty(value = "用户状态：0禁用 1正常",required=true)
    private Integer status;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "用户名")
    private String username;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "用户性别M(男 1) or F(女 0)")
    private String sex;

    @ApiModelProperty(value = "用户手机号")
    private String userMobile;

    @ApiModelProperty(value = "用户手机号隐藏位数")
    private String mobile;

    @ApiModelProperty(value = "头像图片路径")
    private String pic;

    @ApiModelProperty(value = "用户生日")
    private String birthDate;
    /**
     * 用户等级
     */
    @ApiModelProperty(value = "用户等级")
    private Integer level;
    /**
     * 等级条件 0 普通会员 1 付费会员
     */
    @ApiModelProperty(value = "等级条件 0 普通会员 1 付费会员")
    private Integer levelType;
    /**
     * 用户当前成长值
     */
    @ApiModelProperty(value = "用户当前成长值")
    private Integer growth;
    /**
     * 用户积分
     */
    @ApiModelProperty(value = "用户积分")
    private Long score;
    /**
     * 用户余额
     */
    @ApiModelProperty(value = "用户余额")
    private Double balance;
}
