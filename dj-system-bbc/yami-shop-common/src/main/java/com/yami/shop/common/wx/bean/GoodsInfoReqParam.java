/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.common.wx.bean;

import lombok.Data;

/**
 * 微信小程序直播请求参数
 * @author yami
 */
@Data
public class GoodsInfoReqParam extends WxInterfaceInfo{

    private String coverImgUrl;

    private String name;

    private Integer priceType;

    private Double price;

    private Double price2;

    private String url;

    private String requestUrl;

    /**
     *  微信商品id
     */
    private Long goodsId;
    @Override
    public String getRequestUrl() {
        return "/add?access_token=";
    }

}
