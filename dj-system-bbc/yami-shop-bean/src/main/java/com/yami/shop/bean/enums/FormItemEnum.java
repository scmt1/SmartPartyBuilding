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
 * 满减规则
 *
 */
/**
 * @author Yami
 */
public enum FormItemEnum {
    /** 客单价 (支付金额/支付订单数)*/
    CUSTOMER_UNIT_PRICE(1,"客单价","Customer Unit Price",0),

    /** 总成交金额 */
    TOTAL_TRANSACTION_AMOUNT(2,"总成交金额","Total transaction amount",0),

    /** 自营客单价 (自营金额/自营订单数) */
    SELF_OPERATED_CUSTOMER_UNIT_PRICE(3,"自营客单价","Self-employed customer unit price",1),

    /** 自营金额 */
    SELF_OPERATED_AMOUNT(4,"自营金额","Self-employed amount",1),

    /** 自营订单数 */
    SELF_OPERATED_ORDER_NUMS(5,"自营订单数","Number of self-operated orders",1),

    /** 自营商品数 */
    SELF_OPERATED_PRODUCT_NUMS(6,"自营商品数","Number of self-operated products",1),

    /** 下单金额 */
    ORDER_AMOUNT(7,"下单金额","Order amount",0),

    /** 下单人数 */
    USER_NUMS(8,"下单人数", "Number of people placing orders",0),

    /** 下单笔数 */
    ORDER_NUMS(9,"下单笔数","Number of orders",0),

    /** 下单商品数 */
    PRODUCT_NUMS(10,"下单商品数","Number of products ordered",0),

    /** 支付金额 */
    PAY_AMOUNT(11,"支付金额","Payment amount",0),

    /** 支付人数 */
    PAY_USER_NUMS(12,"支付人数","Number of people paid",0),

    /** 支付订单数 */
    PAY_ORDER_NUMS(13,"支付订单数","Number of paid orders",0),

    /** 支付商品数 */
    PAY_PRODUCT_NUMS(14,"支付商品数","Number of paid products",0),

    /** 成功退款金额 */
    SUCCESS_REFUND_AMOUNT(15,"成功退款金额","Successful refund amount",0),

    /** 退款订单数量 */
    REFUND_ORDER_NUMS(16,"退款订单数量","Number of refund orders",0),

    /** 下单-支付转化率 （支付订单数量/下单订单数量）*/
    ORDER_TO_PAY_RATE(17,"下单-支付转化率","Order-Payment Conversion Rate",0),

    /** 退款率 （退款订单数量/订单数量）*/
    REFUND_RATE(18,"退款率","Refund Rate",0);
//    //报表数据分析的相关字段，功能未完善
//    /** 浏览量*/
//    VISIT(19,"浏览量",1),
//
//    /** 跳失率*/
//    LOSS_RATE(20,"跳失率",1),
//
//    /** 平均浏览量*/
//    AVERAGE_VISIT(21,"平均浏览量",1),
//
//    /** 平均停留时长（秒）*/
//    AVERAGE_STOP_TIME(22,"平均停留时长（秒）",1),
//
//    /** 分享访问人数*/
//    SHARE_VISIT_USER(23,"分享访问人数",1),
//
//    /** 分享访问次数*/
//    SHARE_VISIT_NUM(24,"分享访问次数",1),
//
//    /** 访问客数*/
//    VISIT_USER(25,"访问客数",1),
//
//    /** 访问-支付转化率*/
//    VISIT_PAY_RATE(26,"访问客数",1),
//
//    /** 访问-下单转化率*/
//    VISIT_PLACE_ORDER_RATE(27,"访问客数",1);

    private final Integer num;

    private final String name;

    private final String nameEn;

    /**
     * 0:通用，1：平台  2：商家端
     */
    private final Integer type;

    public Integer value() {
        return num;
    }

    public String getName() {
        return name;
    }

    public String getNameEn() { return nameEn; }

    public Integer getType() {
        return type;
    }

    FormItemEnum(Integer num, String name, String nameEn, Integer type){
        this.num = num;
        this.name = name;
        this.nameEn = nameEn;
        this.type = type;
    }

    public static FormItemEnum instance(Integer value) {
        FormItemEnum[] enums = values();
        for (FormItemEnum statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }

    public static FormItemEnum[] allEnum() {
        return values();
    }
}
