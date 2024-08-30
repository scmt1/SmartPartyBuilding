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

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 *
 *
 * @author YXF
 * @date 2020-09-08 10:42:39
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("tz_user_balance_coupon")
public class UserBalanceCoupon implements Serializable{
    private static final long serialVersionUID = 1L;

    /**
     * 余额充值优惠券关联id
     */
    private Long balanceId;
    /**
     * 优惠券id
     */
    private Long couponId;
    /**
     * 优惠券数量
     */
    private Integer couponNum;

}
