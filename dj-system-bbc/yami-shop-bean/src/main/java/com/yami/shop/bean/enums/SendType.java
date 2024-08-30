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
 * 1.订单催付 2.付款成功通知 3.商家同意退款 4.商家拒绝退款 5.核销提醒  6.发货提醒  7.拼团失败提醒 8.拼团成功提醒 9.拼团开团提醒 10.开通会员提醒
 * 11.余额退款成功提醒
 * 101.退款临近超时提醒 102.确认收货提醒 103.买家发起退款提醒 104.买家已退货提醒
 * @author lhd
 * @date 2020-08-27 17:55:57
 */
public enum SendType {
    /**
     * 自定义消息
     */
    CUSTOMIZE(0, 1,"自定义消息", null, null),
    /**
     * 订单催付
     */
    PRESS_PAY(1, 2,"订单催付", null, null),

    /**
     * 付款成功通知
     */
    PAY_SUCCESS(2, 2,"付款成功通知", null, null),

    /**
     * 商家同意退款
     */
    AGREE_REFUND(3, 2,"商家同意退款", null, null),

    /**
     * 商家拒绝退款
     */
    REFUSE_REFUND(4, 2,"商家拒绝退款", null, null),

    /**
     * 核销提醒
     */
    WRITE_OFF(5, 2,"核销提醒", null, null),

    /**
     * 发货提醒
     */
    DELIVERY(6, 2,"发货提醒", null, null),

    /**
     * 拼团失败提醒
     */
    GROUP_FAIL(7, 2,"拼团失败提醒", null, null),

    /**
     * 拼团成功提醒
     */
    GROUP_SUCCESS(8, 2,"拼团成功提醒", null, null),

    /**
     * 拼团开团提醒
     */
    GROUP_START(9, 2,"拼团开团提醒", null, null),

    /**
     * 会员升级通知
     */
    MEMBER(10, 1,"会员升级通知", null, null),

    /**
     * 退款临近超时提醒
     */
    REFUND_OUT_TIME(11, 2,"退款临近超时提醒", null, null),
    /**
     * 用户注册验证码
     */
    REGISTER(12, 1,"用户注册验证码", null, null),
    /**
     * 发送登录验证码
     */
    LOGIN(13, 1,"发送登录验证码", null, null),
    /**
     * 修改密码验证码
     */
    UPDATE_PASSWORD(14, 1,"修改密码验证码", null, null),
    /**
     * 身份验证验证码
     */
    VALID(15, 1,"身份验证验证码", null, null),
    /**
     * 订单退款到余额提醒
     */
    ORDER_REFUND_TO_BALANCE(16, 1,"订单退款到余额提醒", null, null),

    /**
     * 确认收货提醒
     */
    RECEIPT_ORDER(102, 2,"确认收货提醒", "订单", "用户将商品退回后，需要签收"),

    /**
     * 买家发起退款提醒
     */
    LAUNCH_REFUND(103, 2,"买家发起退款提醒", "订单", "用户申请售后，需要商家处理"),

    /**
     * 买家已退货提醒
     */
    RETURN_REFUND(104, 2,"买家已退货提醒", "订单", "买家已退货"),

    /**
     * 用户支付成功，发送给卖家
     */
    USER_PAY_SUCCESS(105, 2, "待发货提醒", "订单", "用户支付成功后,需要发货"),

    /**
     * 商品下架提醒
     */
    PRODUCT_OFFLINE(106, 2,"商品下架提醒", "商品", "平台下架商品后"),

    /**
     * 商品审核结果提醒
     */
    PRODUCT_AUDIT(107, 2,"商品审核结果提醒", "商品", "平台审核商品后"),

    /**
     * 待采购提醒
     */
    TO_PURCHASE(108, 2,"待采购提醒", "订单", "用户的实付金额低于供货价，这笔订单转成待采购订单时会给商家提醒"),


    /**
     * 待收货提醒
     */
    TO_RECEIPT_ORDER(109, 2,"待收货提醒", "订单", "用户将商品退回后，需要签收"),

    /**
     * 营销活动下架
     */
    ACTIVITY_OFFLINE(110, 2,"营销活动下架", "营销活动", "平台下架营销活动后"),

    /**
     * 活动审核结果提醒
     */
    ACTIVITY_AUDIT(111, 2,"活动审核结果提醒", "营销活动", "平台审核营销活动后")

    ;

    private Integer value;
    /**
     * 1为全部平台发送的消息，2为根据情况,3为单独站内信通知
     */
    private Integer type;
    private String desc;
    private final String menu;
    private final String nodeName;
    public Integer getType() {
        return type;
    }
    public Integer getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
    public String getMenu() {
        return menu;
    }

    public String getNodeName() {
        return nodeName;
    }

    SendType(Integer value,Integer type, String desc, String menu, String nodeName) {
        this.value = value;
        this.type = type;
        this.desc = desc;
        this.menu = menu;
        this.nodeName = nodeName;
    }

    public static SendType instance(Integer value) {
        SendType[] enums = values();
        for (SendType statusEnum : enums) {
            if (statusEnum.getValue().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }
}
