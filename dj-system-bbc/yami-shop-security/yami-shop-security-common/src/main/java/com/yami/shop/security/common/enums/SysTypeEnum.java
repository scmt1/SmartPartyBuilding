/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.common.enums;

/**
 * 系统类型
 * @author FrozenWatermelon
 * @date 2020/7/2
 */
public enum SysTypeEnum {

    /**
     * 普通用户系统
     */
    ORDINARY(0),

    /**
     * 商家端
     */
    MULTISHOP(1),

    /**
     * 平台端
     */
    PLATFORM(2),

    /**
     * 自提点
     */
    STATION(3)

    ;

    private final Integer value;

    public Integer value() {
        return value;
    }

    SysTypeEnum(Integer value) {
        this.value = value;
    }

}
