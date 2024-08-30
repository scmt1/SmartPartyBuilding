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
 * 职称类型
 * @author cl
 */
public enum PositionType {

    /**
     * 店铺超级管理员
     */
    ADMIN(0),
    /**
     * 店员
     */
    STAFF(1),;

    private Integer num;

    public Integer value() {
        return num;
    }

    PositionType(Integer num) {
        this.num = num;
    }

    public static PositionType instance(Integer value) {
        PositionType[] enums = values();
        for (PositionType typeEnum : enums) {
            if (typeEnum.value().equals(value)) {
                return typeEnum;
            }
        }
        return null;
    }


}
