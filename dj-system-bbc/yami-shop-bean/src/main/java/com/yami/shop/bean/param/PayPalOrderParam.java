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
 * @author Yami
 */
@Data
public class PayPalOrderParam {

    /**
     * 订单创建完成后的需要跳转的地址
     */
    private String href;
    /**
     * 在paypal创建完成后的订单id
     */
    private String orderId;


}
