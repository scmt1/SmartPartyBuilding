package me.flyray.bsin.server.utils;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

@Data
public class ShortMessageResult {
    private String code;
    private String msg;
    private String tpId;
    private String msgId;
    private JSONArray invalidList;

    private Integer contNum;
    private Object templates;
    private Object page;
    private Integer temId;
    private String temName;
    private String temContent;
    private Object paramJson;
    private String auditMsg;



    @Override
    public String toString() {
        return "ShortMessageResult{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", tpId='" + tpId + '\'' +
                ", msgId='" + msgId + '\'' +
                ", invalidList=" + invalidList +
                '}';
    }
}
