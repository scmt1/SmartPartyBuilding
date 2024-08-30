/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.search.common.constant;

/**
 * es当中的index
 * @author FrozenWatermelon
 * @date 2020/11/12
 */
public enum EsIndexEnum {

    /**
     * 商品
     */
    PRODUCT("product")
    ;

    private final String value;

    public String value() {
        return value;
    }

    EsIndexEnum(String value) {
        this.value = value;
    }
}
