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
 * 店铺钱包金额类型
 * 金额类型 0 未结算金额 1可提现金额  2冻结金额
 */
/**
 * @author Yami
 */
public enum ShopWalletAmountType {

    /**
     * 未结算金额
     */
    UNSETTLED_AMOUNT(0,"未结算金额","Unsettled amount"),

    /**
     * 可提现金额
     */
    SETTLED_AMOUNT(1, "可用店铺余额","Settle amount"),

    /**
     * 冻结金额
     */
    FREEZE_AMOUNT(2, "冻结金额","Freeze amount"),

    /**
     * 总结算金额
     */
    TOTAL_SETTLED_AMOUNT(3, "总结算金额","Total settle amount"),

    ;

    private final Integer num;

    private final String shopWalletAmountType;

    private final String shopWalletAmountTypeEn;

    public Integer value() {
        return num;
    }

    public String getShopWalletAmountType() {
        return shopWalletAmountType;
    }

    public String getShopWalletAmountTypeEn() {
        return shopWalletAmountTypeEn;
    }

    ShopWalletAmountType(Integer num,String shopWalletAmountType,String shopWalletAmountTypeEn){
        this.num = num;
        this.shopWalletAmountType = shopWalletAmountType;
        this.shopWalletAmountTypeEn = shopWalletAmountTypeEn;
    }

    public static ShopWalletAmountType instance(Integer value) {
        ShopWalletAmountType[] enums = values();
        for (ShopWalletAmountType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
