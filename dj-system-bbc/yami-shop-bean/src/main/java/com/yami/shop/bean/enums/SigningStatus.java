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
 * 签约状态：1：已通过 0待审核 -1未通过
 *
 * @Author lth
 * @Date 2021/8/19 10:54
 */
public enum SigningStatus {
    /**
     * 已通过
     */
    SUCCESS(1),
    /**
     * 未通过
     */
    FAIL(-1),
    /**
     * 待审核
     */
    PENDING_REVIEW(0)
    ;

    private Integer id;

    public Integer value() {
        return id;
    }

    SigningStatus(Integer id){
        this.id = id;
    }

    public static SigningStatus instance(Integer value) {
        SigningStatus[] enums = values();
        for (SigningStatus statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
