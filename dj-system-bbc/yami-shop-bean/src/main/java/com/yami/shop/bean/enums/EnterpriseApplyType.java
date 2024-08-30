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
 * 企业支付申请类型
 */
/**
 * @author Yami
 */
public enum EnterpriseApplyType {

    /**
     * 商户提现
     */
    SHOP_WITHDRAW(1, "商户提现"),

    /**
     * 分销员提现
     */
    DISTRIBUTION_WITHDRAW(2,"分销员提现")
    ;

    private Integer value;

    private String desc;

    public Integer value() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    EnterpriseApplyType(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

    public static EnterpriseApplyType instance(Integer value) {
        EnterpriseApplyType[] enums = values();
        for (EnterpriseApplyType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
