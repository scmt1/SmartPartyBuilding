package me.flyray.bsin.server.domain;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

@Data
public class MessageDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String mobile;
    private JSONObject tpContent;
}
