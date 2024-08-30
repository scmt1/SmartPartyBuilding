/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.param;

import lombok.Data;

/**
 * paypal支付成功回调请求参数
 * @author cl
 */
@Data
public class PayPalReqParam {

    /**
     *
     */
    private String paymentId;
    /**
     * (买家)付款人的Client ID
     */
    private String payerId;

    /**
     *
     */
    private String token;

}
