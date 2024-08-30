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
 * 赠送商品类型
 *
 */

/**
 * @author Yami
 */
public enum GiveawayProdTypeEnum {
    /**
     * 主商品
     */
    MAIN_PROD(1),
    /**
     * 赠送商品
     */
    GIVE_PROD(2);

    private Integer id;

    public Integer value() {
        return id;
    }

    GiveawayProdTypeEnum(Integer id){
        this.id = id;
    }

    public static GiveawayProdTypeEnum instance(String value) {
        GiveawayProdTypeEnum[] enums = values();
        for (GiveawayProdTypeEnum statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }

    public static GiveawayProdTypeEnum[] allEnum() {
        GiveawayProdTypeEnum[] enums = values();
        return enums;
    }

}
