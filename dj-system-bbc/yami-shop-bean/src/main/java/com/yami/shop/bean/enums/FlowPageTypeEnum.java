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
 * 页面统计类型
 *
 */

/**
 * @author Yami
 */
public enum FlowPageTypeEnum {
    /** 页面统计数据*/
    PAGE_DATA(1),
    /** 商品详情页数据 */
    PROD_DATA(2);

    private Integer id;

    public Integer value() {
        return id;
    }

    FlowPageTypeEnum(Integer id){
        this.id = id;
    }

    public static FlowPageTypeEnum instance(String value) {
        FlowPageTypeEnum[] enums = values();
        for (FlowPageTypeEnum statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }

    public static FlowPageTypeEnum[] allEnum() {
        FlowPageTypeEnum[] enums = values();
        return enums;
    }
}
