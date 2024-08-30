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
public enum PaypalPaymentMethodEnum {

    /** credit_card */
    CREDIT_CARD(1,"credit_card"),

    /** paypal */
    PAYPAL(2,"paypal")
    ;


    private Integer num;

    private String methodName;

    public Integer value() {
        return num;
    }

    public String getMethodName() {
        return methodName;
    }

    PaypalPaymentMethodEnum(Integer num, String methodName){
        this.num = num;
        this.methodName = methodName;
    }

    public static PaypalPaymentMethodEnum instance(Integer value) {
        PaypalPaymentMethodEnum[] enums = values();
        for (PaypalPaymentMethodEnum typeEnum : enums) {
            if (typeEnum.value().equals(value)) {
                return typeEnum;
            }
        }
        return null;
    }
}
