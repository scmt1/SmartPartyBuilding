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
 * @author chendt
 * @date 2021/5/13 16:52
 */
public enum ShopWithdrawCashStatus {
    /**
     * 审核中
     */
    WAITAUDIT(0, "审核中"),
    /**
     * 审核成功
     */
    SUCCESSAUDIT(1, "审核成功"),

    /**
     * 审核失败
     */
    FAILAUDIT(-1, "审核失败");

    private Integer num;

    private String text;

    public Integer value() {
        return num;
    }

    public String text() {
        return text;
    }

    ShopWithdrawCashStatus(Integer num, String text) {
        this.num = num;
        this.text = text;
    }

    public static ShopWithdrawCashStatus instance(Integer value) {
        ShopWithdrawCashStatus[] enums = values();
        for (ShopWithdrawCashStatus statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
