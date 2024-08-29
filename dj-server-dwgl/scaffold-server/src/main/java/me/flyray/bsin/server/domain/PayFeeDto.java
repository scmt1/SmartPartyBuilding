package me.flyray.bsin.server.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class PayFeeDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String partyMemberId;
    private String idcard;
    private String realName;
    private String payFee;
}
