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
 * 购买类型
 * @author yami
 */
public enum BuyType {

    /** 购买商品*/
    product(1),

    /** 购买会员*/
    LEVEL(2),

    /** 购买余额*/
    BALANCE(3)
    ;



    private Integer num;


    public Integer value() {
        return num;
    }

    BuyType(Integer num){
        this.num = num;
    }

    public static BuyType instance(Integer value) {
        BuyType[] enums = values();
        for (BuyType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
