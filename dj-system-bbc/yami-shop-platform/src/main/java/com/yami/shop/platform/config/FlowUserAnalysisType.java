/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.platform.config;

/**
 * @author Yami
 */
public enum FlowUserAnalysisType {
    /** 团购商品类型 */
    WEEK(1),

    /** 近30天 */
    MONTH(2),

    /** 自定义 */
    CUSTOM(3);

    private Integer num;

    public Integer value() {
        return num;
    }

    FlowUserAnalysisType(Integer num){
        this.num = num;
    }

    public static FlowUserAnalysisType instance(Integer value) {
        FlowUserAnalysisType[] enums = values();
        for (FlowUserAnalysisType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
