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
 * 商品盘点异常原因
 * @author Citrus
 */
public enum TakeStockExceptionEnum {

    /**
     * 删除商品
     */
    DELETE_PROD(1,"删除商品","Delete product"),

    /**
     * 盘点期间有库存变动
     */
    PROD_CHANGE(2,"盘点期间有库存变动","Inventory movements during the stocktaking period"),

    /**
     * 其他
     */
    OTHER(3,"其他","Other"),
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

    TakeStockExceptionEnum(Integer num,String strCn,String strEn) {
        this.num = num;
        this.strCn = strCn;
        this.strEn = strEn;
    }

    public static TakeStockExceptionEnum instance(Integer value) {
        TakeStockExceptionEnum[] enums = values();
        for (TakeStockExceptionEnum statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
