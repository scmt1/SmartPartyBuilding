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
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 消息记录表
 * @author lhd
 * @date 2020-07-01 15:44:27
 */
@Data
@TableName("tz_notify_log")
public class NotifyLog implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    @ApiModelProperty(value = "消息记录表Id")
    private Long logId;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "通知的用户id")
    private String remindId;

    @ApiModelProperty(value = "店铺id")
    private Long shopId;

    @TableField(exist = false)
    @ApiModelProperty(value = "店铺名称")
    private String shopName;

    @ApiModelProperty(value = "通知类型 1.短信发送 2.公众号订阅消息 3.站内消息")
    private Integer remindType;

    @ApiModelProperty(value = "通知短信类型")
    private Integer sendType;

    @ApiModelProperty(value = "用户手机号")
    private String userMobile;

    @ApiModelProperty(value = "通知模板id")
    private Long templateId;

    @ApiModelProperty(value = "通知内容")
    private String message;

    @ApiModelProperty(value = "是否阅读 1已读 0未读")
    private Integer status;

    @ApiModelProperty(value = "记录时间")
    private Date createTime;

    @ApiModelProperty(value = "订单号")
    private String orderNumber;

    @TableField(exist = false)
    @ApiModelProperty(value = "商品名称")
    private String prodName;

    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "营销活动名称")
    private String activityName;

    @ApiModelProperty(value = "消息相关参数内容")
    private String paramContent;

    @ApiModelProperty("短信模板code")
    @TableField(exist = false)
    private String templateCode;

    @ApiModelProperty("公众号模板code")
    @TableField(exist = false)
    private String mpCode;
}
