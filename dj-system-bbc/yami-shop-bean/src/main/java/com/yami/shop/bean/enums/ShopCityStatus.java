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
 * 同城配送状态
 */
/**
 * @author Yami
 */
public enum ShopCityStatus {

    /**
     * 起送费不够
     */
    INSUFFICIENT_DELIVERY_FEE(-3),
    /**
     * 商家没有配置同城配送信息
     */
    NO_CONFIG(-2),

    /**
     * 不在范围内
     */
    OUT_OF_RANGE(-1),

    /**
     * 可用
     */
    USABLE(1)
    ;

    private Integer num;

    public Integer value() {
        return num;
    }

    ShopCityStatus(Integer num) {
        this.num = num;
    }

    public static ShopCityStatus instance(Integer value) {
        ShopCityStatus[] enums = values();
        for (ShopCityStatus statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
