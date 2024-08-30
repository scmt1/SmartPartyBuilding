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
 * 下架处理事件处理状态
 */
/**
 * @author Yami
 */
public enum OfflineHandleEventStatus {

    /**
     * 平台进行下线
     */
    OFFLINE_BY_PLATFORM(1, "平台进行下线"),

    /**
     * 重新申请
     */
    APPLY_BY_SHOP(2, "商家申请恢复"),

    /**
     * 平台审核通过
     */
    AGREE_BY_PLATFORM(3, "平台审核通过"),
    /**
     * 平台审核不通过
     */
    DISAGREE_BY_PLATFORM(4, "平台审核不通过"),
    ;

    private Integer value;
    private String desc;

    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }

    OfflineHandleEventStatus(Integer value, String desc) {
        this.value = value;
        this.desc = desc;
    }

}



