package me.flyray.bsin.server.utils.payUtils;

import java.io.Serializable;

public class JeepayResult implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer code;

    private String msg;

    private String sign;

    private PayOrderQueryResModel data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public PayOrderQueryResModel getData() {
        return data;
    }

    public void setData(PayOrderQueryResModel data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "JeepayResutl{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", sign='" + sign + '\'' +
                ", data=" + data +
                '}';
    }
}
