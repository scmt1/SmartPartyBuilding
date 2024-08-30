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
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户扩展信息
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
@Data
@TableName("tz_user_extension")
public class UserExtension implements Serializable{
    private static final long serialVersionUID = 1L;
    /**
     *
     */
    @TableId
    private Long userExtensionId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户等级
     */
    private Integer level;
    /**
     * 等级条件 0 普通会员 1 付费会员
     */
    private Integer levelType;
    /**
     * 用户当前成长值
     */
    private Integer growth;
    /**
     * 用户积分
     */
    private Long score;
    /**
     * 用户总余额
     */
    private Double balance;
    /**
     * 用户实际余额
     */
    private Double totalBalance;
    /**
     * 乐观锁
     */
    @Version
    private Integer version;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 用户连续签到天数
     */
    private Integer signDay;
    /**
     * 用户昵称
     */
    @TableField(exist = false)
    private String nickName;
    /**
     * 改变余额数值
     */
    @TableField(exist = false)
    private Double changeBalance;
    /**
     * 用户手机号
     */
    @TableField(exist = false)
    private String userMobile;
}
