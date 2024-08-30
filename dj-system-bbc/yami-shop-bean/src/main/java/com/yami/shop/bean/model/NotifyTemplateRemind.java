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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * 消息提醒设置表
 *
 * @author LGH
 * @date 2022-10-26 15:51:49
 */
@Data
@TableName("tz_notify_template_remind")
@ApiModel("消息提醒设置表")
public class NotifyTemplateRemind implements Serializable{
    private static final long serialVersionUID = 1L;

    @TableId
    private Long notifyTemplateRemindId;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    @ApiModelProperty(value = "消息模板id")
    private Long templateId;
    @ApiModelProperty(value = "店铺id")
    private Long shopId;
    @ApiModelProperty(value = "是否开启提醒 1开启  0关闭")
    private Integer isRemind;

    @ApiModelProperty("消息类型")
    @TableField(exist = false)
    private Integer sendType;

    @ApiModelProperty("公告标题")
    @TableField(exist = false)
    private String title;

    @ApiModelProperty("公告内容")
    @TableField(exist = false)
    private String content;

    @ApiModelProperty("通知内容")
    @TableField(exist = false)
    private String message;

    @ApiModelProperty("所属菜单")
    @TableField(exist = false)
    private String menu;

    @ApiModelProperty("推送节点")
    @TableField(exist = false)
    private String nodeName;



}
