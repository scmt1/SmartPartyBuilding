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
 * 退款申请状态枚举
 * @author Yami
 */

public enum RefundApplyStsType {
    /**
     * 未申请
     */
    UNAPPLY(0, "未申请"),
    /**
     * 已申请
     */
    APPLY(1, "已申请"),
    /**
     * 已完成
     */
    SUCCESS(2, "已完成"),
    /**
     * 失败
     */
    FAIL(-1, "失败"),

    ;
    private Integer value;
    private String desc;

    RefundApplyStsType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public Integer value() {
        return value;
    }

    public String desc() {
        return desc;
    }

    public static RefundApplyStsType instance(Integer value) {
        RefundApplyStsType[] enums = values();
        for (RefundApplyStsType refundApplyStsType : enums) {
            if (refundApplyStsType.value().equals(value)) {
                return refundApplyStsType;
            }
        }
        return null;
    }

}
