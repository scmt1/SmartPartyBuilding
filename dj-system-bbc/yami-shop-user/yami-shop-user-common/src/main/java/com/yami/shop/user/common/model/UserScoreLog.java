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

import java.io.Serializable;
import java.util.Date;

/**
 * 用户积分记录
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
@Data
@TableName("tz_user_score_log")
public class UserScoreLog implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 日志id
     */
    @TableId
    private Long logId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 来源 1 订单 2 等级提升获取 3 签到  4 购物抵扣积分 5 积分过期
     */
    @ApiModelProperty(value = "来源 0.注册送积分 1 订单 2 等级提升获取 3 签到  4 购物抵扣积分 5 积分过期 6余额充值")
    private Integer source;
    /**
     * 业务id
     */
    @ApiModelProperty(value = "业务id")
    private String bizId;
    /**
     * 获取积分数量
     */
    @ApiModelProperty(value = "获取积分数量")
    private Long score;
    /**
     * 出入类型 0=支出 1=收入
     */
    @ApiModelProperty(value = "出入类型 0=支出 1=收入")
    private Integer ioType;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    /**
     * 用户昵称
     */
    @TableField(exist = false)
    private String nickName;

    /**
     * 订单编号
     */
    @TableField(exist = false)
    private String orderNumber;

    /**
     * 积分是否锁定
     */
    @TableField(exist = false)
    private Integer isLock;
}
