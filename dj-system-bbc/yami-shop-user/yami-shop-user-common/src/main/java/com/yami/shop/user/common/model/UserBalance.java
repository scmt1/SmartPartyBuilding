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
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 余额充值级别表
 *
 * @author YXF
 * @date 2020-09-08 10:42:39
 */
@Data
@TableName("tz_user_balance")
public class UserBalance implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId
    private Long balanceId;
    /**
     * 充值余额标题
     */
    private String balanceTitle;
    /**
     * 充值金额
     */
    private Double rechargeAmount;
    /**
     * 背景图片
     */
    private String img;
    /**
     * 赠送金额
     */
    private Double presAmount;
    /**
     * 赠送积分
     */
    private Long presScore;
    /**
     * 赠送成长值
     */
    private Integer presGrowth;
    /**
     * 顺序
     */
    private Integer seq;

    @TableField(exist = false)
    private List<Coupon> couponList;

}
