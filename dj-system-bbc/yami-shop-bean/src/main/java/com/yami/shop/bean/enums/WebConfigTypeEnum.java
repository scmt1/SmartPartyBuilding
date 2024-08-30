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
 * 配置类型（平台端 商家端 PC端 h5端 自提点端）
 * @author cl
 * @date 2021-05-19 20:12:35
 */
public enum WebConfigTypeEnum {

    /**
     * 平台端
     */
    PLATFROM("PLATFROM_WEBSITE_CONFIG"),

    /**
     * 商家端
     */
    MULTISHOP("MULTISHOP_WEBSITE_CONFIG"),

    /**
     * PC端
     */
    PC("PC_WEBSITE_CONFIG"),

    /**
     * h5端
     */
    H5("H5_WEBSITE_CONFIG"),

    /**
     * 自提点端
     */
    STATION("STATION_WEBSITE_CONFIG");

    private final String value;

    public String value() {
        return value;
    }

    WebConfigTypeEnum(String value) {
        this.value = value;
    }

    public static WebConfigTypeEnum instance(String paramKey) {
        WebConfigTypeEnum[] enums = values();
        for (WebConfigTypeEnum typeEnum : enums) {
            if (typeEnum.value().equals(paramKey)) {
                return typeEnum;
            }
        }
        return null;
    }
}
