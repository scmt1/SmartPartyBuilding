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
 * 订单类型
 */
/**
 * @author Yami
 */
public enum OrderType {

    /**
     * 普通订单
     */
    ORDINARY(0),
    /**
     * 团购订单
     */
    GROUP(1),

    /**
     * 秒杀订单
     */
    SECKILL(2),
    /**
     * 积分订单
     */
    SCORE(3)
    ;

    private Integer num;

    public Integer value() {
        return num;
    }

    OrderType(Integer num) {
        this.num = num;
    }

    public static OrderType instance(Integer value) {
        OrderType[] enums = values();
        for (OrderType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
