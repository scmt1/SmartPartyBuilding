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
 * 微信直播间接口类型
 * @author lhd
 */
public enum LiveUserRoleType {
    /**
     * 超级管理员
     */
    SUPER_ADMINISTRATOR( 0),
    /**
     * 管理员
     */
    ADMINISTRATOR( 1),

    /**
     * 主播
     */
    ANCHOR(2),
    /**
     * 运营者
     */
    OPERATORS(3),

    ;


    private Integer num;

    public Integer value() {
        return num;
    }

    LiveUserRoleType(Integer num){
        this.num = num;
    }

    public static LiveUserRoleType instance(Integer value) {
        LiveUserRoleType[] enums = values();
        for (LiveUserRoleType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
