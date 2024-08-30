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
 * 装修模板类型
 * @author lhd
 */
public enum TemplateType {

    /**
     * pc端
     */
    PC(1),
    /**
     * 移动端
     */
    H5(2),

    ;
    private Integer num;


    public Integer value() {
        return num;
    }


    TemplateType(Integer num){
        this.num = num;
    }

    public static TemplateType instance(Integer value) {
        TemplateType[] enums = values();
        for (TemplateType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
