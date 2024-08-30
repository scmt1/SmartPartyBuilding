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
 * 配送类型
 */
/**
 * @author Yami
 */
public enum DvyType {

    /**
     * 快递
     */
    DELIVERY(1),
    /**
     * 自提
     */
    STATION(2),

    /**
     * 无需快递
     */
    NOT_DELIVERY(3),
    /**
     * 同城配送
     */
    SAME_CITY(4)
    ;

    private Integer num;

    public Integer value() {
        return num;
    }

    DvyType(Integer num) {
        this.num = num;
    }

    public static DvyType instance(Integer value) {
        DvyType[] enums = values();
        for (DvyType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
