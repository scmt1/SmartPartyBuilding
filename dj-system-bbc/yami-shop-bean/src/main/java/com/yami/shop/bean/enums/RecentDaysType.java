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
 * 最近几天类型
 */
/**
 * @author Yami
 */
public enum RecentDaysType {

    /**
     * 今天
     */
    TODAY(0),

    /**
     * 最近7天
     */
    RECENT_SEVEN_DAYS(7),

    /**
     * 最近15天
     */
    RECENT_FIFTEEN_DAYS(15),

    /**
     * 最近30天
     */
    RECENT_THIRTY_DAYS(30),

    /**
     * 最近45天
     */
    RECENT_FORTY_FIVE_DAYS(45),

    /**
     * 最近60天
     */
    RECENT_SIXTY_DAYS(60),

    /**
     * 最近90天
     */
    RECENT_NINETY_DAYS(90),

    /**
     * 最近180天
     */
    RECENT_ONE_HUNDRED_AND_EIGHTY_DAYS(180);

    private Integer num;

    public Integer value() {
        return num;
    }

    RecentDaysType(Integer num) {
        this.num = num;
    }

    public static RecentDaysType instance(Integer value) {
        RecentDaysType[] enums = values();
        for (RecentDaysType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }

}
