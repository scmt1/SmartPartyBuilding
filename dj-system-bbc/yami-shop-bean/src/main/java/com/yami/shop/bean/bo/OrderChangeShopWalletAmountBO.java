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
 * @author FrozenWatermelon
 * @date 2021/3/9
 */
@Data
public class OrderChangeShopWalletAmountBO {

    /**
     * 订单状态
     */
    private Integer orderStatus;

    /**
     * 订单编号
     */
    private String orderNumber;

    /**
     * 订单项id
     */
    private Long orderItemId;

    /**
     * 退款单id
     */
    private String refundSn;

    /**
     * 店铺id
     */
    private Long shopId;

    /**
     * 订单 or 订单项 实际支付金额
     */
    private Double actualTotal;

    /**
     * 申请退款金额
     */
    private Double refundAmount;

    /**
     * 平台补贴金额
     */
    private Double platformAllowanceAmount;

    /**
     * 商家优惠金额
     */
    private Double shopReduceAmount;

    /**
     * 平台抽成
     */
    private Double platformCommission;

    /**
     * 平台抽成变化金额
     */
    private Double changePlatformCommission;

    /**
     * 平台抽成比例
     */
    private Double rate;

    /**
     * 分销金额
     */
    private Double distributionAmount;

}
