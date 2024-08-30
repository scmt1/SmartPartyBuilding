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
 * 支付状态
 * @author Yami
 */
public enum PayStatus {

    /**
     * 直接进行退款
     */
    REFUND(-1, "退款"),

    /**
     * 未支付
     */
    UNPAY(0, "未支付"),

    /**
     * 已支付
     */
    PAYED(1, "已支付")

    ;

    private final Integer num;
    private final String name;

    public Integer value() {
        return num;
    }

    PayStatus(Integer num, String name) {
        this.num = num;
        this.name = name;
    }

    public static PayStatus instance(Integer value) {
        PayStatus[] enums = values();
        for (PayStatus statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }


    public static String getStatusName(Integer value) {
        PayStatus payStatus = instance(value);
        if (Objects.isNull(payStatus)) {
            return null;
        }
        return payStatus.name;
    }
}
