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
 * 1：普通商品 2:单品
 *
 * @Author lth
 * @Date 2021/9/24 13:23
 */
public enum StockBillLogItemType {
    /**
     * 普通商品
     */
    COMMON_GOODS(1, "普通商品"),

    /**
     * 单品
     */
    SINGLE_GOODS(2, "单品")
    ;

    private final Integer id;

    private final String remark;

    public Integer value() {
        return id;
    }

    public String text() {
        return remark;
    }

    StockBillLogItemType(Integer id, String remark){
        this.id = id;
        this.remark = remark;
    }

    public static StockBillLogItemType instance(Integer value) {
        StockBillLogItemType[] enums = values();
        for (StockBillLogItemType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
