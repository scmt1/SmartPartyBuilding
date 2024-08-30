/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.user.common.enums;

/**
 * 成长值来源
 *
 * @Author lth
 * @Date 2021/10/19 9:37
 */
public enum GrowthLogSourceEnum {
    /**
     * 系统修改用户成长值
     */
    SYSTEM(0, "系统修改用户成长值", "System modify user growth value"),
    /**
     * 订单确认收货获取成长值
     */
    ORDER_SUCCESS(1, "订单确认收货获取成长值", "Order confirmation and receipt to obtain growth value"),
    /**
     * 订单退款退回成长值
     */
    ORDER_FAIL(2, "订单退款退回成长值", "Order refund returns growth value"),
    /**
     * 余额
     */
    BALANCE(3, "用户充值余额获取的成长值", "The growth value obtained by the user's recharge balance")
    ;

    private String cn;
    private String en;

    private final Integer num;

    public String getEn() {
        return en;
    }

    public String getCn() {
        return cn;
    }

    public Integer value() {
        return num;
    }

    GrowthLogSourceEnum(Integer num, String cn, String en){
        this.num = num;
        this.cn = cn;
        this.en = en;
    }

    public static GrowthLogSourceEnum instance(Integer value) {
        GrowthLogSourceEnum[] enums = values();
        for (GrowthLogSourceEnum sourceEnum : enums) {
            if (sourceEnum.value().equals(value)) {
                return sourceEnum;
            }
        }
        return null;
    }
}
