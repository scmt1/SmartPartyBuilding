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
 * 套餐商品类型
 */
public enum ComboProdType {
    /**
     * 主商品
     */
    MAIN_PROD(1),

    /**
     * 搭配商品
     */
    MATCHING_PROD(2)
    ;

    private final Integer num;

    public Integer value() {
        return num;
    }

    ComboProdType(Integer num){
        this.num = num;
    }

    public static ComboProdType instance(Integer value) {
        ComboProdType[] enums = values();
        for (ComboProdType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
