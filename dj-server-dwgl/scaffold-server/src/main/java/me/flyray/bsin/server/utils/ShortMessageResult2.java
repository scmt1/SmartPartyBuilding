package me.flyray.bsin.server.utils;

import lombok.Data;

@Data
public class ShortMessageResult2 {
    private String code;
    private String msg;
    private String tpId;
    private String msgId;
    private Object invalidList;
}
