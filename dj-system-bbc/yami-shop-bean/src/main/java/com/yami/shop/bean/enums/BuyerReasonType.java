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
 * 退款申请原因
 * @author lhd
 */
public enum BuyerReasonType {

    /**
     * 0.拍错/多拍/不喜欢
     */
    WRONG_SHOOTING(0,"拍错/多拍/不喜欢","Wrong shot / more shots / dislike"),
    /**
     * 1.协商一致退款
     */
    REFUND_BY_CONSENSUS(1,"协商一致退款","Refund by consensus"),
    /**
     *  "pleaseChoose": '请选择',
     *  "wrongShot": '拍错/多拍/不喜欢',
     *  "refundConsensus": '协商一致退款',
     *  "damagedGoods": '商品破损/少件',
     *  "productNot": '商品与描述不符',
     *  "sellerSendsWrong": '卖家发错货',
     *  "qualityProblem": '质量问题',
     *  "pleaseChoose": 'Please choose',
     *  "wrongShot": 'Wrong shot / more shots / dislike',
     *  "refundConsensus": 'Refund by consensus',
     *  "damagedGoods": 'Damaged goods / missing items',
     *  "productNot": 'The product does not match the description',
     *  "sellerSendsWrong": 'Seller sends wrong goods',
     *  "qualityProblem": 'Quality problem',
     */
    DAMAGED_GOODS(2,"商品破损/少件","Damaged goods / missing items"),

    /**
     * 3.商品与描述不符
     */
    PROD_ERROR(3,"商品与描述不符","The product does not match the description"),

    /**
     * 4.卖家发错货
     */
    WRONG_DELIVERY(4,"卖家发错货","Seller sends wrong goods"),

    /**
     * 5.质量问题
     */
    QUALITY_PROBLEM(5,"质量问题","Quality problem"),
    /**
     * 6.其他
     */
    OTHER(6,"其他","other"),

    GROUP_FAILED(7,"拼团失败：系统自动退款","Group failed : The system auto refund")
    ;
    private Integer num;

    private String cn;
    private String en;

    public Integer value() {
        return num;
    }

    public String getEn() {
        return en;
    }

    public String getCn() {
        return cn;
    }

    BuyerReasonType(Integer num, String cn, String en){
        this.num = num;
        this.cn = cn;
        this.en = en;
    }

    public static BuyerReasonType instance(Integer value) {
        BuyerReasonType[] enums = values();
        for (BuyerReasonType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }

    public static BuyerReasonType instance(String value) {
        BuyerReasonType[] enums = values();
        for (BuyerReasonType statusEnum : enums) {
            if (statusEnum.getCn().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
