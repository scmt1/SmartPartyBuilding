package com.yami.shop.bean.enums;

public enum NoticeType {

    /**
     * 平台公告
     */
    TO_MULTISHOP(1,"商家端展示"),

    /**
     * 商城公告
     */
    TO_USER(2, "用户端展示")


    ;

    private final Integer num;

    private final String shopWalletAmountType;

    public Integer value() {
        return num;
    }

    public String getShopWalletAmountType() {
        return shopWalletAmountType;
    }

    NoticeType(Integer num, String shopWalletAmountType){
        this.num = num;
        this.shopWalletAmountType = shopWalletAmountType;
    }

    public static NoticeType instance(Integer value) {
        NoticeType[] enums = values();
        for (NoticeType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}