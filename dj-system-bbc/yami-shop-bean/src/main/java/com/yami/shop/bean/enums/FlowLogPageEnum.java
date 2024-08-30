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
 * 页面编号（编号的顺序对数据没有影响）
 *
 */
/**
 * @author Yami
 */
public enum FlowLogPageEnum {
    /** 首页*/
    HOME(1, "首页"),
    /** 轮播图 */
    CAROUSEL(2, "轮播图"),
    /** 商品详情（普通） */
    PROD_INFO(3, "商品详情（普通）"),
    /** 商品详情（秒杀） */
    PROD_INFO_SECKILL(4, "商品详情（秒杀）"),
    /** 商品详情（团购） */
    PROD_INFO_GROUP_BUG(5, "商品详情（团购）"),
    /** 分类 */
    CATEGORY(6, "分类"),
    /** 满减  */
    DISCOUNT(7, "满减"),
    /** 购物车 */
    SHOP_CAT(8, "购物车"),
    /** 订单详情 */
    PLACE_ORDER(9, "订单详情"),
    /** 支付页面 */
    PAY(10, "支付页面"),
    /** 支付成功 */
    PAY_SUCCESS(11, "支付成功"),
    /** 个人中心 */
    USER_CENTER(12, "个人中心"),
    /** 订单列表 */
    ORDER_LIST(13, "订单列表");

    private Integer id;
    private String name;

    public Integer value() {
        return id;
    }
    public static String name(Integer id) {
        FlowLogPageEnum[] enums = values();
        for (FlowLogPageEnum statusEnum : enums) {
            if (statusEnum.value().equals(id)) {
                return statusEnum.name;
            }
        }
        return null;
    }

    FlowLogPageEnum(Integer id, String name){
        this.id = id;
        this.name = name;
    }

    public static FlowLogPageEnum instance(String value) {
        FlowLogPageEnum[] enums = values();
        for (FlowLogPageEnum statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }

    public static FlowLogPageEnum[] allEnum() {
        FlowLogPageEnum[] enums = values();
        return enums;
    }


    public static Integer getProdType(Integer id) {
        if (id.equals(PROD_INFO_GROUP_BUG.value())){
            return 1;
        }
        if (id.equals(PROD_INFO_SECKILL.value())){
            return 2;
        }
        return 0;
    }

    public static Boolean isProdInfo(Integer id) {
        if (id.equals(PROD_INFO.value()) ||id.equals(PROD_INFO_GROUP_BUG.value()) || id.equals(PROD_INFO_SECKILL.value())){
            return true;
        }
        return false;
    }
    public static Boolean isPayOrPaySuccess(Integer id) {
        if (id.equals(PAY.value()) ||id.equals(PAY_SUCCESS.value())){
            return true;
        }
        return false;
    }
}
