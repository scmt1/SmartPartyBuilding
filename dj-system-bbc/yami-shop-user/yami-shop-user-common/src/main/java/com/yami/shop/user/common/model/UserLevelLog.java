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

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
@Data
@TableName("tz_user_level_log")
public class UserLevelLog implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    @ApiModelProperty("等级记录表")
    private Long levelLogId;

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("用户头像")
    @TableField(exist = false)
    private String pic;

    @ApiModelProperty("用户名")
    @TableField(exist = false)
    private String userName;

    @ApiModelProperty("用户名称")
    @TableField(exist = false)
    private String nickName;

    @ApiModelProperty("升级之后的等级")
    private Integer level;

    @ApiModelProperty("等级名称")
    private String levelName;

    @ApiModelProperty("等级类型 0 普通会员 1 付费会员")
    private Integer levelType;

    @ApiModelProperty("升级时间")
    private Date createTime;

    @ApiModelProperty("起始时间")
    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createStartTime;

    @ApiModelProperty("结束时间")
    @TableField(exist = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createEndTime;

    @ApiModelProperty("礼物发放状态(0发送中 1发送已完成)")
    private Integer state;

    @ApiModelProperty("礼物发放时间")
    private Date giftTime;

    @ApiModelProperty("快递方式")
    private Long devId;

    @ApiModelProperty("快递公司")
    @TableField(exist = false)
    private String dvyName;

    @ApiModelProperty("物流编号")
    private String devNo;

    @ApiModelProperty("订单收货地址id")
    private Long addrOrderId;

    @ApiModelProperty("支付单号")
    private String payNo;

    @ApiModelProperty("是否支付1已支付0未支付")
    private Integer isPayed;

    @ApiModelProperty("支付方式")
    private Integer payType;

    @ApiModelProperty("支付金额")
    private Double payAmount;

    @ApiModelProperty("有效期数")
    private Integer term;

    @ApiModelProperty("期数类型 1:日 2:周 3:月 4:季 5:年")
    private Integer termType;

}
