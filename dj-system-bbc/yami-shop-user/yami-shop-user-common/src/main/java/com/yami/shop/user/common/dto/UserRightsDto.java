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

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.yami.shop.common.serializer.json.ImgJsonSerializer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 会员等级权益
 *
 * @author LHD
 * @date 2020-02-26 16:03:14
 */
@Data
public class UserRightsDto implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 权益id
     */
    @ApiModelProperty(value = "权益id")
    private Long rightsId;
    /**
     * 权益名称
     */
    @ApiModelProperty(value = "权益名称")
    private String rightsName;
    /**
     * 权益图标
     */
    @JsonSerialize(using = ImgJsonSerializer.class)
    @ApiModelProperty(value = "权益名称")
    private String icon;
    /**
     * 权益简介
     */
    @ApiModelProperty(value = "权益名称")
    private String description;
    /**
     * 0系统核销 1商家线下核销
     */
    @ApiModelProperty(value = "0系统核销 1商家线下核销")
    private Integer serviceType;
    /**
     * 状态：-1:禁用 1：正常(仅用于系统核销)
     */
    private Integer status;
    /**
     * 顺序
     */
    private Integer seq;
}
