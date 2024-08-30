/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @author YXF
 * @date 2020-07-17 09:50:13
 */
@Data
@TableName("tz_flow_user_analysis")
public class FlowUserAnalysis implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户流量id")
    @TableId
    private Long flowUserAnalysisId;

    @ApiModelProperty("创建日期")
    private Date createDate;

    @ApiModelProperty("用户Id")
    private String userId;

    @ApiModelProperty("省Id")
    private Long provinceId;

    @ApiModelProperty("加购数量")
    private Integer plusShopCart;

    @ApiModelProperty("下单金额")
    private Double placeOrderAmount;

    @ApiModelProperty("支付金额")
    private Double payAmount;

    @ApiModelProperty("浏览量")
    private Integer visitNums;

    @ApiModelProperty("停留时长")
    private Long stopTime;

    @ApiModelProperty("1:新用户  0:旧用户")
    private Integer newUser;

    @ApiModelProperty("会话数")
    private Integer sessionNums;

    @ApiModelProperty("浏览商品Id字符串")
    private String visitProdIds;

    @ApiModelProperty("系统类型   1:pc  2:h5  3:小程序 4:安卓  5:ios")
    private Integer systemType;

    @ApiModelProperty("uuid")
    @TableField(exist = false)
    private String uuid;

    @ApiModelProperty("会话数set")
    @TableField(exist = false)
    private Set<String> sessionSet;

    @ApiModelProperty("浏览商品集合")
    @TableField(exist = false)
    private Set<String> visitProdSet;
}
