package com.yami.shop.bean.bo;

import lombok.Data;

/**
 * 支付后，微信/支付宝返回的一些基础数据
 * @author 菠萝凤梨
 */
@Data
public class PayInfoResultBO {

    /**
     * 商城支付单号
     */
    private String payNo;

    /**
     * 第三方订单流水号
     */
    private String bizPayNo;

    /**
     * 第三方订单订单号
     */
    private String bizOrderNo;

    /**
     * 是否支付成功
     */
    private Boolean isPaySuccess;

    /**
     * 支付成功的标记
     */
    private String successString;

    /**
     * 支付金额
     */
    private Double payAmount;

    /**
     * 回调内容
     */
    private String callbackContent;
}
