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

import java.util.Objects;

/**
 * 支付入口 0订单 1充值 2开通会员
 * @author Yami
 */
public enum PayEntry {

    /**
     * 订单
     */
    ORDER(0, "订单"),

    /**
     * 充值
     */
    RECHARGE(1, "充值"),

    /**
     * 开通会员
     */
    VIP(2, "开通会员")
    ;

    private final Integer num;
    private final String payName;

    public Integer value() {
        return num;
    }

    PayEntry(Integer num, String payName) {
        this.num = num;
        this.payName = payName;
    }

    public static PayEntry instance(Integer value) {
        PayEntry[] enums = values();
        for (PayEntry statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }

    public static String getPayTypeName(Integer value) {
        PayEntry payEntry = instance(value);
        if (Objects.isNull(payEntry)) {
            return null;
        }
        return payEntry.payName;
    }
}
