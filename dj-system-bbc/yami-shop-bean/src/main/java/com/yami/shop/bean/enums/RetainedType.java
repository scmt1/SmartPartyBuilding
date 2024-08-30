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
 * 留存类型 1访问留存  2成交留存
 * @author cl
 * @date 2021-05-25 16:01:18
 */
public enum RetainedType {

    /**
     * 访问留存
     */
    VISIT_RETAINED(1),

    /**
     * 成交留存
     */
    TRADE_RETAINED(2)

    ;

    private Integer num;

    public Integer value() {
        return num;
    }

    RetainedType(Integer num) {
        this.num = num;
    }

    public static RetainedType instance(Integer value) {
        RetainedType[] enums = values();
        for (RetainedType typeEnum : enums) {
            if (typeEnum.value().equals(value)) {
                return typeEnum;
            }
        }
        return null;
    }
}
