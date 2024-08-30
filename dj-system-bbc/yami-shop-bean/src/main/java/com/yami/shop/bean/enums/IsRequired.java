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
 * @author lth
 *
 * 套餐商品是否必选
 */
public enum IsRequired {
    /**
     * 必选
     */
    YES(1),

    /**
     * 不是必选
     */
    NO(0)
    ;

    private final Integer num;

    public Integer value() {
        return num;
    }

    IsRequired(Integer num){
        this.num = num;
    }

    public static IsRequired instance(Integer value) {
        IsRequired[] enums = values();
        for (IsRequired statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
