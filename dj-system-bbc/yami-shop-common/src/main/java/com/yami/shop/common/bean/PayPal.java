/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.common.bean;

import lombok.Data;

/**
 * Paypal支付配置
 * @author cl
 */
@Data
public class PayPal {

    /**
     *  PayPal 的 clientId
     */
    private String clientId;

    /**
     *  PayPal 的 clientSecret
     */
    private String clientSecret;

    /**
     * sandbox 沙箱or live生产
     */
    private String mode;
    /**
     *  商家邮箱
     */
    private String receiverEmail;
    /**
     * 事件 id
     */
    private String webHookId;

}
