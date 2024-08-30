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
 * 交易用户类型
 * @Author lth
 * @Date 2021/7/28 14:36
 */
public enum TransactionUserType {

    /**
     * 新成交用户
     */
    NEW_TRANSACTION_USE(1),

    /**
     * 旧成交用户
     */
    OLD_TRANSACTIONUSERTYPE(2),
    ;

    private final Integer value;

    public Integer value() {
        return value;
    }

    TransactionUserType(Integer value) {
        this.value = value;
    }
}
