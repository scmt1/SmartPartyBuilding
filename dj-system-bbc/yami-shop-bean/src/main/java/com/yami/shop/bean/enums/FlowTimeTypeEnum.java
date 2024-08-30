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
 * 页面编号（编号的顺序对数据没有影响）
 *
 */
/**
 * @author Yami
 */
public enum FlowTimeTypeEnum {
    /** 日*/
    DAY(1),
    /** 周 */
    WEEK(2),
    /** 月 */
    MONTH(3),
    /** 今日实时 */
    REAL_TIME(4),
    /** 近七天 */
    SEVEN_DAYS(5),
    /** 近30天 */
    ONE_MONTH(6),
    /** 自定义 */
    CUSTOM(7);

    private Integer id;

    public Integer value() {
        return id;
    }

    FlowTimeTypeEnum(Integer id){
        this.id = id;
    }

    public static FlowTimeTypeEnum instance(String value) {
        FlowTimeTypeEnum[] enums = values();
        for (FlowTimeTypeEnum statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }

    public static FlowTimeTypeEnum[] allEnum() {
        FlowTimeTypeEnum[] enums = values();
        return enums;
    }
}
