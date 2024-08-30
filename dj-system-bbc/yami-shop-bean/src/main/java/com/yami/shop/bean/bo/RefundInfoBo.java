/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.bo;

import lombok.Data;

/**
 * @author Yami
 */
@Data
public class RefundInfoBo {

    /**
     * 商城支付单号
     */
    private String refundNo;

    /**
     * 第三方订单退款流水号
     */
    private String bizRefundNo;

    private Boolean isRefundSuccess;

    private String successString;

    private String callbackContent;
}
