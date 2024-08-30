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
public enum RefundStatusEnum {

    /**
     * 申请退款
     */
    APPLY(1),
    /**
     * 退款成功
     */
    SUCCEED(2),
    /**
     * 部分退款成功
     */
    PARTIAL_SUCCESS(3),
    /**
     * 退款失败
     */
    DISAGREE(4);

    private Integer num;

    RefundStatusEnum(Integer num) {
        this.num = num;
    }

    public Integer value() {
        return num;
    }
}
