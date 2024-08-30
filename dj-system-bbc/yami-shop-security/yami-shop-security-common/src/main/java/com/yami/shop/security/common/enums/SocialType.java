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
 * 第三方系统类型
 * @author FrozenWatermelon
 * @date 2021/01/16
 */
public enum SocialType {

    /**
     * 普通用户系统
     */
    ORDINARY(0),

    /**
     * 小程序
     */
    MA(1),

    /**
     * 公众号
     */
    MP(2),

    ;

    private final Integer value;

    public Integer value() {
        return value;
    }

    SocialType(Integer value) {
        this.value = value;
    }

}
