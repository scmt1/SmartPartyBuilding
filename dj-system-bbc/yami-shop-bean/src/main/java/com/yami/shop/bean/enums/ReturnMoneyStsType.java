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
 * @author Yami
 */
public enum ReturnMoneyStsType {

    /**
     * 退款申请中
     */
    APPLY(1),

    /**
     * 卖家处理退款
     */
    PROCESSING(2),

    /**
     * 买家已发货
     */
    CONSIGNMENT(3),

    /**
     * 卖家已收货
     */
    RECEIVE(4),

    /**
     * 退款成功
     */
    SUCCESS(5),

    /**
     * 客户撤回退款申请
     */
    CANCEL(6),

    /**
     * 商家拒绝
     */
    REJECT(7),

    /**
     * 退款关闭
     */
    FAIL(-1);

    private Integer num;

    ReturnMoneyStsType(Integer num) {
        this.num = num;
    }

    public Integer value() {
        return num;
    }

    public static ReturnMoneyStsType instance(Integer value) {
        ReturnMoneyStsType[] enums = values();
        for (ReturnMoneyStsType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
