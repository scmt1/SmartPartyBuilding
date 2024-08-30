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

import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * 微信直播商品响应参数
 * @author yami
 */
@Data
public class WxLiveProdResponse<T> implements Serializable {


    private int errcode;

    private Integer total;

    private String msg;

    private T goods;

    private Long roomId;

    public boolean isSuccess(){
        return Objects.equals(ResponseCode.WX_SUCCESS, this.errcode);
    }


}
