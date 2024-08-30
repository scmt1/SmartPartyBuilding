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
 * 短信类型
 * @author LGH
 */
public enum SmsType {

    /**
     * 用户注册验证码
     */
    REGISTER(0,"SMS_151810562","验证码${code}，您正在注册成为新用户，感谢您的支持！"),

    /**
     * 发送登录验证码
     */
    LOGIN(1,"SMS_151810564","验证码${code}，您正在登录，若非本人操作，请勿泄露。"),
    /**
     * 修改密码验证码
     */
    UPDATE_PASSWORD(2,"SMS_151810561","验证码${code}，您正在尝试修改登录密码，请妥善保管账户信息。"),
    /**
     * 身份验证验证码
     */
    VALID(3,"SMS_151810565","验证码${code}，您正在进行身份验证，打死不要告诉别人哦！"),

//    /**
//     * 商品库存不足通知
//     */
//    STOCKS_ARM(1,"SMS_152288054","尊敬的${name}，感谢您对xxx的支持。您的${prodName}库存仅剩${num}，为免影响您的客户下单，请及时补货！"),
//
//    /**
//     * 用户发货通知
//     */
//    NOTIFY_DVY(2,"SMS_152283152","尊敬的${name}，感谢您对xxx的支持。您的订单${orderNumber}已通过${dvyName}发货，快递单号是：${dvyFlowId}。请注意查收。"),
//
//    /**
//     * 普通用户下单成功通知
//     */
//    USER_BUY_SUCCESS(3,"SMS_152288329","亲爱的客户，感谢您对xxx的支持。您的订单${orderNumber}已支付成功。我们会尽快发货!")

    ;

    private Integer num;

    private String templateCode;

    private String content;
    public Integer value() {
        return num;
    }

    SmsType(Integer num,String templateCode,String content){
        this.num = num;
        this.templateCode = templateCode;
        this.content = content;
    }

    public static SmsType instance(Integer value) {
        SmsType[] enums = values();
        for (SmsType statusEnum : enums) {
            if (statusEnum.value().equals(value)) {
                return statusEnum;
            }
        }
        return null;
    }

    public String getTemplateCode() {
        return this.templateCode;
    }

    public String getContent() {
        return this.content;
    }
}
