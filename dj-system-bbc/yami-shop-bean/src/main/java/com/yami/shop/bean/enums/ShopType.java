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
 * 0普通店铺 1优选好店
 * @author cl
 * @date 2021-06-05 11:14:08
 */
public enum ShopType {

    /**
     * 普通店铺
     */
    GENERAL_STORE(0, "普通店铺"),

    /**
     * 优选好店
     */
    PREFERRED_STORES(1, "优选好店")
    ;

    private Integer num;

    private String text;

    public Integer value() {
        return num;
    }

    public String text() {
        return text;
    }

    ShopType(Integer num, String text) {
        this.num = num;
        this.text = text;
    }

    public static ShopType instance(Integer value) {
        ShopType[] enums = values();
        for (ShopType typeEnum : enums) {
            if (typeEnum.value().equals(value)) {
                return typeEnum;
            }
        }
        return null;
    }
}
