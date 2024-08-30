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
public enum LiveRoomStatusType {
    /**
     * 直播中
     */
    LIVING(101),
    /**
     * 未开始
     */
    NO_START(102),

    /**
     * 已结束
     */
    FINISHED(103),

    /**
     * 禁播
     */
    NO_LIVE(104),
    /**
     * 暂停
     */
    PAUSE(105),
    /**
     * 异常
     */
    EXCEPTION(106),
    /**
     * 已过期
     */
    EXPIRED(107)
    ;


    private Integer num;

    public Integer value() {
        return num;
    }

    LiveRoomStatusType(Integer num){
        this.num = num;
    }

    public static LiveRoomStatusType instance(Integer value) {
        LiveRoomStatusType[] enums = values();
        for (LiveRoomStatusType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
