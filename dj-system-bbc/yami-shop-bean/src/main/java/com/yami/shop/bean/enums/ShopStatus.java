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
 * 店铺状态
 */
/**
 * @author Yami
 */
public enum ShopStatus {

    /**
     * 已删除
     */
    NOTOPEN(-1, "已删除"),

    /**
     * 停业中
     */
    STOP(0, "停业中"),

    /**
     * 营业中
     */
    OPEN(1, "营业中"),

    /**
     * 违规下线
     */
    OFFLINE(2, "违规下线"),

    /**
     * 违规审核
     */
    OFFLINE_AUDIT(3,  "上线待审核"),

    /**
     * 开店申请中
     */
    APPLYING(4, "开店申请中"),

    /**
     * 开店申请待审核
     */
    OPEN_AWAIT_AUDIT(5, "开店申请待审核")

    ;

    private Integer num;

    private String text;

    public Integer value() {
        return num;
    }

    public String text() {
        return text;
    }

    ShopStatus(Integer num, String text) {
        this.num = num;
        this.text = text;
    }

    public static ShopStatus instance(Integer value) {
        ShopStatus[] enums = values();
        for (ShopStatus statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
