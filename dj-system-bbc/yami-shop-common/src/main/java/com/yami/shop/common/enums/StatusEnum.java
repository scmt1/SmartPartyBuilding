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

import java.util.Objects;

/**
 * 状态
 * @author yxf
 * @date 2020/11/20
 */
public enum StatusEnum {

    /**
     * 删除 (逻辑删除)
     */
    DELETE(-1),

    /**
     * 禁用/过期/下架
     */
    DISABLE(0),

    /**
     * 启用/未过期/上架
     */
    ENABLE(1),

    /**
     * 违规下架/未启用
     */
    OFFLINE(2),

    /**
     * 等待审核
     */
    WAIT_AUDIT(3),

    /**
     * 关闭
     */
    FINISHED(4)
    ;

    private final Integer value;

    public Integer value() {
        return value;
    }

    StatusEnum(Integer value) {
        this.value = value;
    }

    public static StatusEnum instance(Integer value) {
        StatusEnum[] enums = values();
        for (StatusEnum statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }

    public static Boolean offlineStatus (Integer value) {
        StatusEnum[] enums = values();
        for (StatusEnum statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public static Boolean offlineOrDelete (Integer status) {
        if (Objects.equals(status, OFFLINE.value) || Objects.equals(status, DELETE.value) || Objects.equals(status, DISABLE.value)) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}
