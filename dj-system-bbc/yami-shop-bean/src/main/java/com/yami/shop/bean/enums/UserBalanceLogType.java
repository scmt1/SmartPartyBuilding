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
 * 会员余额记录类型
 * @Author lth
 * @Date 2021/7/28 14:36
 */
public enum UserBalanceLogType {

    /**
     * 充值
     */
    RECHARGE(1),

    /**
     * 赠送
     */
    GIVE(2),

    /**
     * 支付
     */
    PAY(3),

    /**
     * 店铺自定义品牌
     */
    REFUND(4),

    /**
     * 店铺自定义品牌
     */
    PLATFORM_EDIT(5),

    /**
     * 充值会员
     */
    BUY_MEMBER(6)
    ;

    private final Integer value;

    public Integer value() {
        return value;
    }

    UserBalanceLogType(Integer value) {
        this.value = value;
    }
}
