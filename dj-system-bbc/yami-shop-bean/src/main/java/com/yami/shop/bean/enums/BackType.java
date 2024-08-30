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
 * 支付回调类型
 */
/**
 * @author Yami
 */
public enum BackType {

    /**
     * api
     */
    API(0),
    /**
     * 商家端
     */
    SHOP(1),

    /**
     * 平台端
     */
    PLATFORM(2)
    ;

    private Integer num;

    public Integer value() {
        return num;
    }

    BackType(Integer num) {
        this.num = num;
    }

    public static BackType instance(Integer value) {
        BackType[] enums = values();
        for (BackType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
