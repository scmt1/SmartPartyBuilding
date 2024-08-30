package com.yami.shop.bean.vo;

import lombok.Data;

/**
 * 订单金额
 * @author 菠萝凤梨
 */
@Data
public class OrderAmountVO {
    /**
     * 支付金额
     */
    private Double payAmount;

    /**
     * 支付积分
     */
    private Long payScore;
}
