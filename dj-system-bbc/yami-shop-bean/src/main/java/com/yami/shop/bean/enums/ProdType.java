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
 * 商品类型
 *
 */
/**
 * @author Yami
 */
public enum ProdType {
    /** 普通商品类型 */
    PROD_TYPE_NORMAL(0,"普通商品"),

    /** 团购商品类型 */
    PROD_TYPE_GROUP(1,"团购商品"),

    /** 秒杀商品类型 */
    PROD_TYPE_SECKILL(2,"秒杀商品"),

    /** 积分商品类型 */
    PROD_TYPE_SCORE(3,"积分商品"),

    /** 活动商品类型 */
    PROD_TYPE_ACTIVE(5,"活动商品");

    private Integer num;
    private String desc;

    public Integer value() {
        return num;
    }

    public String desc() {
        return desc;
    }

    ProdType(Integer num, String desc){
        this.num = num;
        this.desc = desc;
    }

    public static ProdType instance(Integer value) {
        ProdType[] enums = values();
        for (ProdType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
