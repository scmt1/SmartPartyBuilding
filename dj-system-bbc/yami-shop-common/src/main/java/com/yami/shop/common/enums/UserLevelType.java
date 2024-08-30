/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.common.enums;

/**
 * 会员等级类型
 *
 * @author yami
 */
public enum UserLevelType {
    /** 普通会员 */
    ORDINARY(0),

    /** vip会员 */
    VIP(1),
    ;

    private Integer num;

    public Integer value() {
        return num;
    }

    UserLevelType(Integer num){
        this.num = num;
    }

    public static UserLevelType instance(Integer value) {
        UserLevelType[] enums = values();
        for (UserLevelType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
