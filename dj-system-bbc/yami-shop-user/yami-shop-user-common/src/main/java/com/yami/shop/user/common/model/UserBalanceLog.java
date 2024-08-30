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

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 余额充值记录
 *
 * @author YXF
 * @date 2020-09-09 17:38:30
 */
@Data
@TableName("tz_user_balance_log")
public class UserBalanceLog implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 充值记录id
     */
    @TableId
    private Long balanceLogId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 改变余额
     */
    private Double changeBalance;
    /**
     * 收支类型 0支出 1收入
     */
    private Integer ioType;
    /**
     * 支付单号
     */
    private String payNo;
    /**
     * 1:充值 2:赠送 3:支付 4:退款 5平台手动修改 6.充值会员
     */
    private Integer type;
    /**
     * 是否支付1已支付0未支付
     */
    private Integer isPayed;
    /**
     * 充值余额id
     */
    private Long balanceId;

    /**
     * 退款单号
     */
    private String refundSn;

}
