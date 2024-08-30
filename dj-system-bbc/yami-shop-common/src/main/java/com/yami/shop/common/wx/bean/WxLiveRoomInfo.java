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
 * 微信直播间接口所需参数
 * @author LHD
 */
@Data
public class WxLiveRoomInfo extends WxInterfaceInfo{

    /**
     * 直播间名称
     */
    private String name;
    /**
     * 主播昵称
     */
    private String anchorName;
    /**
     * 主播微信号
     */
    private String anchorWechat;
    /**
     * 背景图
     */
    private String coverImg;
    /**
     * 主播分享图
     */
    private String shareImg;
    /**
     * 购物直播频道封面图
     */
    private String feedsImg;
    /**
     * 是否开启官方收录
     */
    private Integer isFeedsPublic;

    /**
     * 直播间类型 1.推流 2.手机直播
     */
    private Integer type;

    /**
     * 0.竖屏 1.横屏
     */
    private Integer screenType;
    /**
     * 直播间功能
     */
    private String roomTools;
    /**
     * 购物直播频道封面图微信mediaId
     */
    private String feedsImgId;
    /**
     * 购物直播频道封面图微信mediaId
     */
    private String coverImgId;
    /**
     * 购物直播频道封面图微信mediaId
     */
    private String shareImgId;
    /**
     * 直播开始时间
     */
    private Long wxStartTime;
    /**
     * 直播结束时间
     */
    private Long wxEndTime;
    /**
     * 是否关闭客服 0 开启，1 关闭
     */
    private Integer closeKf;
    /**
     * 是否关闭点赞  0 开启，1 关闭
     */
    private Integer closeLike;
    /**
     * 是否关闭货架  0 开启，1 关闭
     */
    private Integer closeGoods;
    /**
     * 是否关闭评论  0 开启，1 关闭
     */
    private Integer closeComment;
    /**
     * 是否关闭回放  0 开启，1 关闭
     */
    private Integer closeReplay;
    /**
     * 是否关闭分享  0 开启，1 关闭
     */
    private Integer closeShare;


    @Override
    public String getRequestUrl() {
        return "/wxaapi/broadcast/room/create?access_token=";
    }

}
