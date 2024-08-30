/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.common.response;

/**
 * @author FrozenWatermelon
 * @date 2020/7/9
 */
public enum ResponseEnum {

    /**
     * ok
     */
    OK("00000", "ok"),

    /**
     * 用于直接显示提示用户的错误，内容由输入内容决定
     */
    SHOW_FAIL("A00001", ""),

    /**
     * 用于直接显示提示系统的成功，内容由输入内容决定
     */
    SHOW_SUCCESS("A00002", ""),

    /**
     * 未授权
     */
    UNAUTHORIZED("A00004", "Unauthorized"),

    /**
     * 服务器出了点小差
     */
    EXCEPTION("A00005", "服务器出了点小差"),
//
//    /**
//     * 验证码有误
//     */
//    VERIFICATION_CODE_ERROR("A00006", "验证码有误或已过期"),
//
//    /**
//     * 数据异常
//     */
//    DATA_ERROR("A00007", "数据异常，请刷新后重新操作"),
//
//    /**
//     * 一些需要登录的接口，而实际上因为前端无法知道token是否已过期，导致token已失效时，
//     * 应该返回一个状态码，告诉前端token已经失效了，及时清理
//     */
//    CLEAN_TOKEN("A00008", "clean token"),
//
//    /**
//     * 刷新token已过期
//     */
//    REFRESH_TOKEN_EXIST("A00009", "refresh token exist"),
//
//    /**
//     * 数据不完整
//     */
//    DATA_INCOMPLETE("A00010", "数据不完整"),
//
//    /**
//     * 有敏感词
//     */
//    SENSITIVE_WORD("A00011","存在敏感词，请重新输入"),

    /**
     * TempUid异常
     * 一般不会出现这个异常，出现这个异常会有两种已知可能
     * 1. 一种是旧的tempUid
     * 2. 一种是同域名的localstorage 有个也叫tempUid的存储覆盖了（有的人测试环境和正式环境放在同一个域名不同子目录下）
     * 如果前端看到返回了这个异常，为了让用户能够顺利登录，需要重新获取一遍code，重新获取tempUid
     */
    TEMP_UID_ERROR("A00012", "TempUid Error"),

    /**
     * 接口不存在
     */
    NOT_FOUND("A00013", "接口不存在"),

    /**
     * 方法参数没有校验，内容由输入内容决定
     */
    METHOD_ARGUMENT_NOT_VALID("A00014", "方法参数没有校验"),

    /**
     * 01开头代表商品
     * 商品已下架，返回特殊的状态码，用于渲染商品下架的页面
     */
//    SPU_NOT_EXIST("A01000", "商品不存在"),

    /**
     * 02开头代表购物车
     */
//    SHOP_CART_NOT_EXIST("A02000", "商品已下架"),

    /**
     * 03开头代表订单
     */
//    ORDER_NOT_EXIST("A03000", "订单不存在"),

    /**
     * 订单不支持该配送方式
     */
    ORDER_DELIVERY_NOT_SUPPORTED("A03001", "The delivery method is not supported"),

    /**
     * 请勿重复提交订单，
     * 1.当前端遇到该异常时，说明前端防多次点击没做好
     * 2.提示用户 订单已发生改变，请勿重复下单
     */
    REPEAT_ORDER("A03002", "订单已过期，请重新下单"),

    /**
     * 优惠券不能共用
     */
    COUPON_CANNOT_USE_TOGETHER("A03003", "优惠券不能共用"),

    /**
     * 库存不足，body会具体返回那个skuid的库存不足，后台通过skuId知道哪个商品库存不足，前端不需要判断
     */
    NOT_STOCK("A03010", "not stock"),

    /**
     * 该社交账号被其他用户绑定了，如果返回这个状态码，前端应该提示用户解绑已经绑定的账号重新绑定
     */
    SOCIAL_ACCOUNT_BIND_BY_OTHER("A04002", "social account bind by other"),
    ;

    private final String code;

    private final String msg;

    public String value() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    ResponseEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "ResponseEnum{" + "code='" + code + '\'' + ", msg='" + msg + '\'' + "} " + super.toString();
    }

}
