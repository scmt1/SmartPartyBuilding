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
 * 出入库类型
 *
 * @Author lth
 * @Date 2021/9/9 13:38
 */
public enum StockType {

    /**
     * 出库
     */
    OUT_OF_STOCK(1, "出库"),

    /**
     * 入库
     */
    WAREHOUSING(2, "入库");

    private final Integer id;

    private final String remark;

    public Integer value() {
        return id;
    }

    public String text() {
        return remark;
    }

    StockType(Integer id, String remark){
        this.id = id;
        this.remark = remark;
    }

    public static StockType instance(Integer value) {
        StockType[] enums = values();
        for (StockType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
