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
 * 满减规则
 *
 */
/**
 * @author Yami
 */
public enum DiscountType {

    /**
     * 按满足最高层级减一次
     * M money
     */
    MAX(0),

    /**
     * 每满一次减一次
     */
    TRIGGER(1)
    ;

    private Integer num;

    public Integer value() {
        return num;
    }

    DiscountType(Integer num){
        this.num = num;
    }

    public static DiscountType instance(Integer value) {
        DiscountType[] enums = values();
        for (DiscountType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
