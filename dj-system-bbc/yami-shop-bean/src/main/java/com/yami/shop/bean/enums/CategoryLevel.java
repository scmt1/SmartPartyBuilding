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
 * 分类等级
 * @author yami
 */
public enum CategoryLevel {

    /**
     * 第一级
     */
    First(0),

    /**
     * 第二级
     */
    SECOND(1),

    /**
     * 第三级
     */
    THIRD(2)
    ;



    private Integer num;


    public Integer value() {
        return num;
    }

    CategoryLevel(Integer num){
        this.num = num;
    }

    public static CategoryLevel instance(Integer value) {
        CategoryLevel[] enums = values();
        for (CategoryLevel statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
