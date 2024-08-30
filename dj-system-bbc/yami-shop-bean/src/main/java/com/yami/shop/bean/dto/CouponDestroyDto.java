package com.yami.shop.bean.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CouponDestroyDto {
    @NotEmpty(message = "优惠券id不能为空")
    private String couponId;
    @NotEmpty(message = "支付单号不能为空")
    private String payNo;
    @NotEmpty(message = "订单号不能为空")
    private String orderNumber;
    @NotEmpty(message = "订单金额不能为空")
    private Double payMoney;
}
