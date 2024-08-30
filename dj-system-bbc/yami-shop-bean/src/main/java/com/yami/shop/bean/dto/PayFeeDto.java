package com.yami.shop.bean.dto;

import lombok.Data;

@Data
public class PayFeeDto {
    private String partyId;
    private String idcard;
    private String name;
    private String payFee;
}
