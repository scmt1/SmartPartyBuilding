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
 * 发票抬头类型 1.单位 2.个人
 * @author Pineapple
 * @date 2021/8/2 8:53
 */
public enum InvoiceHeaderType {
    /**
     * 单位
     */
    COMPANY(1),
    /**
     * 个人
     */
    PERSONAL(2);

    private final Integer num;

    InvoiceHeaderType(Integer num) {
        this.num = num;
    }

    public Integer value() {
        return num;
    }

    public static InvoiceHeaderType instance(Integer value) {
        InvoiceHeaderType[] enums = values();
        for (InvoiceHeaderType headerType : enums) {
            if (headerType.value().equals(value)) {
                return headerType;
            }
        }
        return null;
    }
}
