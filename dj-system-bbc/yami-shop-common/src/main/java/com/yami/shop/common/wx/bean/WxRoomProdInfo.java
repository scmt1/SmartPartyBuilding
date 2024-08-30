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
 * 微信直播间商品详情参数
 * @author yami
 */
@Data
public class WxRoomProdInfo extends WxInterfaceInfo{

    private List<Long> ids;

    private Long roomId;

    @Override
    public String getRequestUrl() {
        return "/wxaapi/broadcast/room/addgoods?access_token=";
    }

}
