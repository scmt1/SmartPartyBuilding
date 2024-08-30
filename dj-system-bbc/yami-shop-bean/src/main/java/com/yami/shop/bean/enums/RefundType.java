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
 * @author Yami
 */
public enum RefundType {
    /**
     * 整单退款
     */
    ALL(1),
    /**
     * 单项退款
     */
    SINGLE(2);

    private Integer num;

    RefundType(Integer num) {
        this.num = num;
    }

    public Integer value() {
        return num;
    }

    public static RefundType instance(Integer value) {
        RefundType[] enums = values();
        for (RefundType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
