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
 * 盘点状态
 * @author Citrus
 */
public enum TakeStockBillStatusEnum {

    /**
     * 已作废
     */
    VOIDED(0,"已作废","VOIDED"),

    /**
     * 盘点中
     */
    TAKING(1,"盘点中","TAKING"),

    /**
     * 已完成
     */
    COMPLETED(2,"已完成","COMPLETED"),
    ;

    private final Integer num;
    private final String strCn;
    private final String strEn;

    public Integer value() {
        return num;
    }
    public String strCn() {
        return strCn;
    }
    public String strEn() {
        return strEn;
    }

    TakeStockBillStatusEnum(Integer num,String strCn,String strEn) {
        this.num = num;
        this.strCn = strCn;
        this.strEn = strEn;
    }

    public static TakeStockBillStatusEnum instance(Integer value) {
        TakeStockBillStatusEnum[] enums = values();
        for (TakeStockBillStatusEnum statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
