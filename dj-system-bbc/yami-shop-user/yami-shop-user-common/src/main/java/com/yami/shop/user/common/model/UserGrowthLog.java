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
import java.util.Date;

/**
 * 用户成长值记录
 *
 * @author LGH
 * @date 2020-02-26 16:03:14
 */
@Data
@TableName("tz_user_growth_log")
public class UserGrowthLog implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 成长值获取记录表
     */
    @TableId
    private Long logId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 来源 0：系统修改 1：订单支付 2：订单退款 3：余额充值
     */
    private Integer source;
    /**
     * 关联业务id
     */
    private String bizId;
    /**
     * 变更成长值
     */
    private Integer changeGrowth;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 创建时间
     */
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
    private  String orderNumber;
}
