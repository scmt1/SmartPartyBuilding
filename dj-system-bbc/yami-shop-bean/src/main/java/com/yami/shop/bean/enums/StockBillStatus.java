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
 * @Date 2021/9/9 13:41
 */
public enum StockBillStatus {
    /**
     * 已作废
     */
    ABOLISHED(0, "已作废","Void"),

    /**
     * 已 出/入 库
     */
    SUCCESS(1, "已 出/入 库","Out of storage\n / Stocked"),

    /**
     * 待提交
     */
    AWAIT_SUBMIT(2, "待提交","To be submitted");

    private final Integer id;

    private final String remark;

    private final String remarkEn;

    public Integer value() {
        return id;
    }

    public String text() {
        return remark;
    }

    public String textEn() {return remarkEn; }

    StockBillStatus(Integer id, String remark, String remarkEn){
        this.id = id;
        this.remark = remark;
        this.remarkEn = remarkEn;
    }

    public static StockBillStatus instance(Integer value) {
        StockBillStatus[] enums = values();
        for (StockBillStatus statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
