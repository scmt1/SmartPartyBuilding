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
 * 时间类型 1最近1个月 2最近3个月 3最近6个月 4最近1年
 * @author cl
 * @date 2021-05-25 16:01:18
 */
public enum RetainedDateType {

    /**
     * 最近1个月
     */
    ONE_MONTH(1, 1),

    /**
     * 最近3个月
     */
    THREE_MONTH(2, 3),

    /**
     * 最近6个月
     */
    SIX_MONTH(3, 6),

    /**
     * 最近1年
     */
    ONE_YEAR(4, 12)

    ;

    private Integer num;
    private Integer monthNum;

    public Integer value() {
        return num;
    }
    public Integer getMonthNum() {
        return monthNum;
    }

    RetainedDateType(Integer num, Integer monthNum) {
        this.num = num;
        this.monthNum = monthNum;
    }

    public static RetainedDateType instance(Integer value) {
        RetainedDateType[] enums = values();
        for (RetainedDateType typeEnum : enums) {
            if (typeEnum.value().equals(value)) {
                return typeEnum;
            }
        }
        return null;
    }
}
