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
 * 审核状态
 */
/**
 * @author Yami
 */
public enum AuditStatus {

    /**
     * 未审核
     */
    WAITAUDIT(0),
    /**
     * 已通过
     */
    SUCCESSAUDIT(1),

    /**
     * 未通过
     */
    FAILAUDIT(-1),

    /**
     * 发放成功
     */
    SUCCESS(2),

    /**
     * 发放失败
     */
    FAIL(3)

    ;

    private Integer num;

    public Integer value() {
        return num;
    }

    AuditStatus(Integer num) {
        this.num = num;
    }

    public static AuditStatus instance(Integer value) {
        AuditStatus[] enums = values();
        for (AuditStatus statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
