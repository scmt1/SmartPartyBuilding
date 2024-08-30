package com.yami.shop.bean.model.dj;

import lombok.Data;

@Data
public class PartyResponseEntity {
    private String msg;
    private String code;
    private Boolean isException;
    private Object data;
}
