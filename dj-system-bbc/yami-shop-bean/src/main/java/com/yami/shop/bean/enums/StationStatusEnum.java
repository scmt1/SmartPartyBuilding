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
 * @Date 2021/11/23 10:05
 */
public enum StationStatusEnum {
    /**
     * 关闭
     */
    CLOSURE(0, "关闭"),

    /**
     * 营业
     */
    OPEN(1, "营业"),

    /**
     * 强制关闭
     */
    FORCED_SHUTDOWN(2, "强制关闭"),

    /**
     * 审核中
     */
    UNDER_REVIEW(3, "审核中"),

    /**
     * 审核失败
     */
    AUDIT_FAILURE(4, "审核失败"),

    ;

    private Integer num;

    private String text;

    public Integer value() {
        return num;
    }

    public String text() {
        return text;
    }

    StationStatusEnum(Integer num, String text) {
        this.num = num;
        this.text = text;
    }

    public static StationStatusEnum instance(Integer value) {
        StationStatusEnum[] enums = values();
        for (StationStatusEnum statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
