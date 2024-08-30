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
public enum RefundApplyType {
    /**
     * 仅退款
     */
    REFUND_ONLY(1),
    /**
     * 退货退款
     */
    RETURN_REFUND(2);

    private Integer num;

    RefundApplyType(Integer num) {
        this.num = num;
    }

    public Integer value() {
        return num;
    }

    public static RefundApplyType instance(Integer value) {
        RefundApplyType[] enums = values();
        for (RefundApplyType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
