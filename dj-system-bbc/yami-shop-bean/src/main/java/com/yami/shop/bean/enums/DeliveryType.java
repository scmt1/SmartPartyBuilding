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
 * 商品物流选择
 */
/**
 * @author Yami
 */
public enum DeliveryType {

    /**
     * 快递
     */
    EXPRESS(1, "快递配送"),
    /**
     * 自提
     */
    STATION(2,"自提"),
    /**
     * 无需快递
     */
    NO_EXPRESS(3,"无需快递"),
    /**
     * 同城配送
     */
    SAME_CITY(4,"同城配送"),
    ;

    private Integer value;
    private String desc;

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    DeliveryType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static String getDescription(Integer value){
        DeliveryType[] enums = values();
        for (DeliveryType deliveryType : enums) {
            if (deliveryType.getValue().equals(value)){
                return deliveryType.getDesc();
            }
        }
        return null;
    }
}



