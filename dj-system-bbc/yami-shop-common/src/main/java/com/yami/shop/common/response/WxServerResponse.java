/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.common.response;

import cn.binarywang.wx.miniapp.json.WxMaGsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.yami.shop.common.wx.bean.resp.LiveUserRespInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 微信响应参数
 * @author yami
 */
@Data
public class WxServerResponse<T> implements Serializable {


    private int errcode;

    private Integer total;

    private String msg;

    @SerializedName("list")
    private List<LiveUserRespInfo> list;

    private T roomInfo;

    private Long roomId;

    public boolean isSuccess(){
        return Objects.equals(ResponseCode.WX_SUCCESS, this.errcode);
    }

    public static WxServerResponse fromJson(String json) {
        return WxMaGsonBuilder.create().fromJson(json, WxServerResponse.class);
    }

}
