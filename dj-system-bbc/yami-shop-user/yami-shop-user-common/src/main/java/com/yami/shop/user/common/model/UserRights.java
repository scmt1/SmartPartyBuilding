/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.common.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 会员等级
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
@Data
@TableName("tz_user_rights")
public class UserRights implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    @ApiModelProperty("权益id")
    private Long rightsId;

    @ApiModelProperty("权益名称")
    private String rightsName;

    @ApiModelProperty("权益图标")
    private String icon;

    @ApiModelProperty("权益简介")
    private String description;

    @ApiModelProperty("0系统核销 1商家线下核销")
    private Integer serviceType;

    @ApiModelProperty("状态：-1:禁用 1：正常(仅用于系统核销)")
    private Integer status;

    @ApiModelProperty("顺序")
    private Integer seq;
}
