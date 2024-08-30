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
 * 通知类型 1.短信发送 2.公众号订阅消息 3.站内消息
 * @author Yami
 */
public enum RemindType {


    /**
     * 短信发送
     */
    SMS(1),

    /**
     * 公众号订阅消息
     */
    MP(2),

    /**
     * 站内消息
     */
    MINI(3)

    ;

    private Integer num;

    public Integer value() {
        return num;
    }

    RemindType(Integer num) {
        this.num = num;
    }

    public static RemindType instance(Integer value) {
        RemindType[] enums = values();
        for (RemindType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
