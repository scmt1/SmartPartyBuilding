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
import com.yami.shop.bean.param.UserTagParam;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 *
 * @author lhd
 * @date 2020-07-01 15:44:27
 */
@Data
@TableName("tz_notify_template")
public class NotifyTemplate implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    @ApiModelProperty("模板id")
    private Long templateId;
    /**
     * 0.自定义消息 1.订单催付 2.付款成功通知 3.商家同意退款 4.商家拒绝退款 5.核销提醒  6.发货提醒  7.拼团失败提醒 8.拼团成功提醒 9.拼团开团提醒 10.开通会员提醒
     * 101.退款临近超时提醒 102.确认收货提醒 103.买家发起退款提醒 104.买家已退货提醒
     */
    @ApiModelProperty("消息类型")
    private Integer sendType;

    @ApiModelProperty("通知内容")
    private String message;

    @ApiModelProperty("短信模板code")
    private String templateCode;

    @ApiModelProperty("消息发送类型 1.平台自行发送类型 2.商家")
    private Integer msgType;

    @ApiModelProperty("公众号模板code")
    private String mpCode;

    @ApiModelProperty("用通知方式集合用逗号分隔 1.短信 2.公众号 3.站内消息")
    private String templateTypes;

    @ApiModelProperty("模板类型列表")
    @TableField(exist = false)
    private List<Integer> templateTypeList;

    @ApiModelProperty("店铺模板类型列表")
    @TableField(exist = false)
    private List<Integer> shopTemplateTypeList;

    @TableField(exist = false)
    private Boolean sms = false;

    @TableField(exist = false)
    private Boolean app= false;

    @TableField(exist = false)
    private Boolean sub= false;

    @TableField(exist = false)
    private Boolean shopSms = false;

    @TableField(exist = false)
    private Boolean shopApp= false;

    @TableField(exist = false)
    private Boolean shopSub = false;

    @TableField(exist = false)
    private List<Long> selTagIds;

    @TableField(exist = false)
    private List<UserTagParam> tagParams;

    @ApiModelProperty("启用状态 1启用 0禁用")
    private Integer status;

    @ApiModelProperty("创建时间")
    private Date createTime;

}
