/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.dto;


import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户流量记录
 *
 * @author YXF
 * @date 2020-07-13 13:18:33
 */
@Data
@TableName("tz_flow_analysis_log")
public class FlowLogDto implements Serializable{
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "uuid")
    private String uuid;

    @ApiModelProperty(value = "会话uuid")
    private String uuidSession;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "系统类型 1:pc  2:h5  3:小程序 4:安卓  5:ios")
    private Integer systemType;

    @ApiModelProperty(value = "页面编号")
    private Integer pageId;

    @ApiModelProperty(value = "访问类型 1:页面访问  2:分享访问  3:页面点击  4:加入购物车")
    private Integer visitType;

    @ApiModelProperty(value = "业务数据（商品页：商品id；支付界面：订单编号数组；支付成功界面：订单编号数组）")
    private String bizData;

    @ApiModelProperty(value = "业务类型（商品页：商品类型）")
    private Integer bizType;

    @ApiModelProperty(value = "用户操作步骤数")
    private Integer step;

    @ApiModelProperty(value = "用户操作数")
    private Integer nums;
}
