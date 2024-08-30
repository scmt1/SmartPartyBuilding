/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.enums;

/**
 * 余额支付类型
 * @Author lth
 * @Date 2021/7/28 14:36
 */
public enum OrderTimeStatusType {

    /**
     * 全部订单
     */
    ALL(0),

    /**
     * 拼团订单
     */
    GROUP_BUY(1),

    /**
     * 秒杀订单
     */
    SECKILL(2),

    /**
     * 普通订单
     */
    ORDINARY(3)
    ;

    private final Integer value;

    public Integer value() {
        return value;
    }

    OrderTimeStatusType(Integer value) {
        this.value = value;
    }
}
