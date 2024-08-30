/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.bean.pay;

import com.yami.shop.common.enums.PayType;
import lombok.Data;

import java.util.List;

/**
 * 支付信息
 * @author LGH
 */
@Data
public class RefundInfoDto {

    /**
     * 支付方式
     */
    private PayType payType;

    /**
     * 支付单号
     */
    private String payNo;
    /**
     * 外部支付单号
     */
    private String bizPayNo;

    /**
     * 支付单号
     */
    private String refundSn;

    /**
     * 付款金额
     */
    private Double payAmount;

    /**
     * 退款金额
     */
    private Double refundAmount;

    /**
     * 通知结果
     */
    private String notifyUrl;
    /**
     * 通知结果
     */
    private String userId;

    /**
     * 是否直接退款
     */
    private Integer onlyRefund;

    /**
     * 多个退款的订单编号
     */
    private List<String> refundOrderNumbers;
}
