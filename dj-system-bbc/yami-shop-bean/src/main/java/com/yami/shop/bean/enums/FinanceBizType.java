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
 * 财务对账的业务类型
 * @author Yami
 */
public enum FinanceBizType {

    /**
     * 订单支付
     */
    ORDER(1, "订单支付","Order Payment"),
    /**
     * 订单退款
     */
    REFUND(2, "订单退款","Order Refund"),

    /**
     * 余额充值
     */
    BALANCE(3, "余额充值","Balance recharge"),

    /**
     * 购买会员
     */
    LEVEL(4, "购买会员","Buy Membership")
    ;

    private final Integer num;

    private final String nameCn;

    private final String nameEn;

    public Integer value() {
        return num;
    }
    public String getNameCn() {
        return nameCn;
    }
    public String getNameEn() {
        return nameEn;
    }
    FinanceBizType(Integer num, String nameCn, String nameEn) {
        this.num = num;
        this.nameCn = nameCn;
        this.nameEn = nameEn;
    }

    public static FinanceBizType instance(Integer value) {
        FinanceBizType[] enums = values();
        for (FinanceBizType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
