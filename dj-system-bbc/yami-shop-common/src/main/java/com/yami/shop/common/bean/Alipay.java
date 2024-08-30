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
 * 支付宝配置
 * @author lgh
 */
@Data
public class Alipay {

    private String appId;

    /**
     * 应用公钥证书
     */
    private String appCertPath;

    /**
     * 支付宝公钥证书
     */
    private String alipayCertPath;

    /**
     * 支付宝根证书
     */
    private String alipayRootCertPath;

    /**
     * 应用私钥
     */
    private String appPrivateKey;
}
