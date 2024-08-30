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
public enum SystemEnum {
    /** PC*/
    PC(1, "PC"),
    /** H4 */
    H5(2, "H5"),
    /** 小程序 */
    APPLETS(3, "微信小程序"),
    /** 安卓 */
    ANDROID(4, "安卓"),
    /** IOS */
    IOS(5, "ios");

    private Integer id;
    private String name;

    public Integer value() {
        return id;
    }

    public String getName() {
        return name;
    }

    SystemEnum(Integer id, String name){
        this.id = id;
        this.name = name;
    }

    public static SystemEnum instance(String value) {
        SystemEnum[] enums = values();
        for (SystemEnum statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }

}
