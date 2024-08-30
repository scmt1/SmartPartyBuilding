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
 * 企业支付状态
 */
/**
 * @author Yami
 */
public enum EnterprisePayStatus {

    /**
     * 申请中
     */
    APPLY(1, "申请中"),
    /**
     * 成功
     */
    SUCCESS(2,"已完成"),
    /**
     * 满减
     */
    FAIL(-1,"失败"),

    ;

    private Integer value;
    private String desc;

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    EnterprisePayStatus(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

}



