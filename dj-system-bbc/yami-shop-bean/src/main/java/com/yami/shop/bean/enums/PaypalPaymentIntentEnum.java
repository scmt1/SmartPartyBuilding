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
public enum PaypalPaymentIntentEnum {

    /** sale */
    SALE(1,"sale"),

    /** authorize */
    AUTHORIZE(2,"authorize"),

    /** order */
    ORDER(3,"order")
    ;



    private Integer num;

    private String paymentIntentName;

    public Integer value() {
        return num;
    }

    public String getPaymentIntentName() {
        return paymentIntentName;
    }

    PaypalPaymentIntentEnum(Integer num, String paymentIntentName){
        this.num = num;
        this.paymentIntentName = paymentIntentName;
    }

    public static PaypalPaymentIntentEnum instance(Integer value) {
        PaypalPaymentIntentEnum[] enums = values();
        for (PaypalPaymentIntentEnum typeEnum : enums) {
            if (typeEnum.value().equals(value)) {
                return typeEnum;
            }
        }
        return null;
    }

}
