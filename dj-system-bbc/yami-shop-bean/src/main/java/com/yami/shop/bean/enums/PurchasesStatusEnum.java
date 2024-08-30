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
 * 地区层级
 * @author cl
 */
public enum PurchasesStatusEnum {


    /**
     * 已作废
     */
    VOIDED(0),

    /**
     * 待入库
     */
    WAIT_STOCK(1),

    /**
     * 部分入库
     */
    PARTIALLY_STOCK(2),

    /**
     * 已完成
     */
    COMPLETION(3)

    ;

    private Integer num;

    public Integer value() {
        return num;
    }

    PurchasesStatusEnum(Integer num) {
        this.num = num;
    }

    public static PurchasesStatusEnum instance(Integer value) {
        PurchasesStatusEnum[] enums = values();
        for (PurchasesStatusEnum statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
