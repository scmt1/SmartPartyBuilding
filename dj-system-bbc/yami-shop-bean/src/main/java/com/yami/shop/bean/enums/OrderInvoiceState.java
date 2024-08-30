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
 * 发票状态 1.申请中 2.已开票 3.失败
 * @author Pineapple
 * @date 2021/8/2 8:51
 */
public enum OrderInvoiceState {

    /**
     * 申请中
     */
    APPLICATION(1),
    /**
     * 已开票
     */
    ISSUED(2),
    /**
     * 失败
     */
    FAIL(3);

    private final Integer num;

    OrderInvoiceState(Integer num) {
        this.num = num;
    }

    public Integer value() {
        return num;
    }

    public static OrderInvoiceState instance(Integer value) {
        OrderInvoiceState[] enums = values();
        for (OrderInvoiceState stateEnum : enums) {
            if (stateEnum.value().equals(value)) {
                return stateEnum;
            }
        }
        return null;
    }
}
