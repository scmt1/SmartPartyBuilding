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
 * 满减规则
 *
 * @author yami
 */
public enum UserRightsInfo {
    /** 打折 */
    DISCOUNT(1L),
    /** 包邮 */
    IS_FREE_FEE(2L),
    /** 送积分 */
    SCORE(3L),
    /** 送优惠券 */
    COUPON(4L),
    /** 积分倍率 */
    RATE_SCORE(5L),
    ;

    private Long id;

    public Long value() {
        return id;
    }

    UserRightsInfo(Long id){
        this.id = id;
    }

    public static UserRightsInfo instance(Integer value) {
        UserRightsInfo[] enums = values();
        for (UserRightsInfo statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
