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
 * 积分详情状态
 * @author Yami
 */
public enum UserScoreStatus {

    /**
     * 过期
     */
    EXPIRED(-1),
    /**
     * 订单抵现
     */
    ORDER_CREDIT(0),

    /**
     * 正常
     */
    NORMAL(1),

    /**
     * 订单抵现已结算
     */
    ORDER_CREDIT_SETTLE(2)
    ;

    private Integer num;

    public Integer value() {
        return num;
    }

    UserScoreStatus(Integer num) {
        this.num = num;
    }
}
