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
 * 订单活动类型（套餐，满减）
 * @author Yami
 */
public enum OrderActivityType {

    /**
     * 无活动
     */
    NULL(0),

    /**
     * 满减
     */
    DISCOUNT(1),
    /**
     * 套餐
     */
    COMBO(2);

    private final Integer type;

    public Integer value() {
        return type;
    }

    OrderActivityType(Integer type){
        this.type = type;
    }

    public static OrderActivityType instance(Integer type) {
        OrderActivityType[] enums = values();
        for (OrderActivityType orderActivityType : enums) {
            if (orderActivityType.value().equals(type)) {
                return orderActivityType;
            }
        }
        return null;
    }
}
