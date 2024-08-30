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

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 *
 * @author
 * @date 2019-10-15 16:44:50
 */
@Data
@TableName("tz_enterprise_pay")
public class EnterprisePay implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 企业支付ID
     */
    @TableId
    private Long entPayId;
    /**
     * 状态(1:申请中 2:已完成 -1:失败)
     */
    private Integer status;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户openid
     */
    private String openId;
    /**
     * 企业支付订单号
     */
    private Long entPayOrderNo;
    /**
     * 金额
     */
    private Double amount;
    /**
     * 支付类型(1:商户提现)
     */
    private Integer type;
    /**
     * 业务id，如商户提现即商户提现记录ID
     */
    private Long bizId;
    /**
     * 版本号
     */
    private Integer version;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 更新时间
     */
    private Date updateTime;

}
