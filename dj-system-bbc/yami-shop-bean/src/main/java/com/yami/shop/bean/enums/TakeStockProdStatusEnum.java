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
 * 商品盘点状态
 *
 * @author Citrus
 */
public enum TakeStockProdStatusEnum {

    /**
     * 盘盈
     */
    PROFIT(1, "盘盈","PROFIT"),
    /**
     * 盘亏
     */
    LOSS(2, "盘亏","LOSS"),

    /**
     * 盘平
     */
    EQUAL(0, "盘平","EQUAL"),
    /**
     * 异常
     */
    EXCEPTION(-1, "异常","EXCEPTIONAL"),
    ;

    private final Integer num;
    private final String str;
    private final String strEn;

    public Integer value() {
        return num;
    }

    public String str() {
        return str;
    }
    public String strEn() {
        return strEn;
    }

    TakeStockProdStatusEnum(Integer num, String str,String strEn) {
        this.num = num;
        this.str = str;
        this.strEn = strEn;
    }

    public static TakeStockProdStatusEnum instance(Integer value) {
        TakeStockProdStatusEnum[] enums = values();
        for (TakeStockProdStatusEnum statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
