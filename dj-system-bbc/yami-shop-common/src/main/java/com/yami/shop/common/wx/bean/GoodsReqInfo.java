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

import java.util.List;

/**
 * 微信小程序直播参数
 * @author yami
 */
@Data
public class GoodsReqInfo extends WxInterfaceInfo{

    /**
     *     商品ID
     */
    private Long goodsId;

    private List<Long> goodsIds;
    /**
     * 审核单ID
     */
    private Long auditId;

    private String requestUrl;

    @Override
    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }
}
