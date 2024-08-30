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
 * 店铺钱包金额发生改变的原因
 * 0用户支付 1用户确认收货 2 用户退款申请 3 拒绝用户退款申请 4 提现申请 5 提现申请被拒绝 6 提现申请通过
 *
 * @author LGH
 */
public enum ShopWalletChangeReason {

    /**
     * 用户支付
     */
    PAY(0, "用户支付","User Payment"),


    /**
     * 订单结算
     */
    SETTLED(1, "订单结算","Order Settlement"),

    /**
     * 用户进行退款
     */
    ORDER_REFUND(2, "用户进行退款","Users make refunds"),

    /**
     * 拒绝用户退款申请
     */
    REJECT_REFUND(3,"拒绝用户退款申请","Refusal of user refund requests"),

    /**
     * 提现申请
     */
    APPLY_CASH(4, "提现申请","Withdrawal request"),

    /**
     * 提现申请被拒绝
     */
    REFUSE_CASH(5, "提现申请被拒绝","Withdrawal request is rejected"),

    /**
     * 通过提现申请
     */
    PASS_CASH(6, "提现申请通过","Cash withdrawal request approved"),

    /**
     * 系统发放成功
     */
    SUCCESS_CASH(7, "系统发放成功",""),
    /**
     * 提现发放失败
     */
    FAIL_CASH(8, "系统发放失败",""),

    /**
     * 系统扣除订单中需要颁发给用户的分销金额
     */
    DISTRIBUTION_AMOUNT(9, "扣除订单分销金额","");

    private final Integer num;

    private final String shopWalletChangeReason;

    private final String shopWalletChangeReasonEn;

    public Integer value() {
        return num;
    }

    public String getShopWalletChangeReason() {
        return shopWalletChangeReason;
    }

    public String getShopWalletChangeReasonEn() {
        return shopWalletChangeReasonEn;
    }

    ShopWalletChangeReason(Integer num, String shopWalletChangeReason, String shopWalletChangeReasonEn) {
        this.num = num;
        this.shopWalletChangeReason = shopWalletChangeReason;
        this.shopWalletChangeReasonEn = shopWalletChangeReasonEn;
    }

    public static ShopWalletChangeReason instance(Integer value) {
        ShopWalletChangeReason[] enums = values();
        for (ShopWalletChangeReason statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
