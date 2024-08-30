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
 * @Author lth
 * @Date 2021/8/19 10:28
 */
public enum SigningType {
    /**
     * 分类
     */
    CATEGORY(1),
    /**
     * 品牌
     */
    BRAND(2);

    private Integer id;

    public Integer value() {
        return id;
    }

    SigningType(Integer id){
        this.id = id;
    }

    public static SigningType instance(Integer value) {
        SigningType[] enums = values();
        for (SigningType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
