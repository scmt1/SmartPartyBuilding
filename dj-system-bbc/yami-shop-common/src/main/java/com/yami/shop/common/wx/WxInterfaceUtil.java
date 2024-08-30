/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.common.wx;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.yami.shop.common.exception.YamiShopBindException;
import com.yami.shop.common.i18n.I18nMessage;
import com.yami.shop.common.response.WxLiveProdResponse;
import com.yami.shop.common.response.WxServerResponse;
import com.yami.shop.common.util.Json;
import com.yami.shop.common.util.PageParam;
import com.yami.shop.common.wx.bean.*;
import com.yami.shop.common.wx.bean.resp.GoodsInfoRespParam;
import com.yami.shop.common.wx.bean.resp.GoodsListRespParam;
import com.yami.shop.common.wx.bean.resp.RoomDetailResponse;
import com.yami.shop.common.wx.bean.resp.RoomResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 微信相关工具类
 * @author LHD
 */
@Slf4j
@Component
public class WxInterfaceUtil {



    private String url;
    private String wxImgUrl = "https://api.weixin.qq.com/cgi-bin/media";
    private String liveUrl = "https://api.weixin.qq.com";
    private String goodsUrl = "https://api.weixin.qq.com/wxaapi/broadcast/goods";
    private String goodsStatusUrl = "https://api.weixin.qq.com/wxa/business";
    private String goodsListUrl = "https://api.weixin.qq.com/wxaapi/broadcast/goods/getapproved?access_token=[ACCESS_TOKEN]";

    public static String GET_GOODS_WARE_HOUSE = "getgoodswarehouse";
    public static String GET_APPROVED = "getapproved";
    public static String ERR_MSG = "errmsg";


//    /**
//     * 上传临时素材接口
//     * @return
//     */
//    public ServerResponse<ImageResponse> uploadMedia(WxInterfaceInfo creditInfo){
//        Map<String, Object> requestParam = new HashMap<>(16);
////        requestParam.put("type", creditInfo.getType());
//        // 微信token
////        requestParam.put("access_token", creditInfo.getAccessToken());
//        requestParam.put("media", creditInfo.getByteArray());
//
//        return signAndSend(creditInfo, requestParam,new TypeReference<ServerResponse<ImageResponse>>() {});
//
//    }

    /**
     * 增加直播间接口
     * @return
     */
    public WxServerResponse<Void> addLiveRoom(WxLiveRoomInfo wxLiveRoomInfo){
        Map<String, Object> requestParam = new HashMap<>(16);
        requestParam.put("name",wxLiveRoomInfo.getName());
        requestParam.put("coverImg",wxLiveRoomInfo.getCoverImgId());
        requestParam.put("startTime",wxLiveRoomInfo.getWxStartTime());
        requestParam.put("endTime",wxLiveRoomInfo.getWxEndTime());
        requestParam.put("anchorName",wxLiveRoomInfo.getAnchorName());
        requestParam.put("anchorWechat",wxLiveRoomInfo.getAnchorWechat());
        requestParam.put("shareImg",wxLiveRoomInfo.getShareImgId());
        requestParam.put("feedsImg",wxLiveRoomInfo.getFeedsImgId());
        requestParam.put("isFeedPublic",wxLiveRoomInfo.getIsFeedsPublic());
        requestParam.put("type",wxLiveRoomInfo.getType());
        requestParam.put("screenType",wxLiveRoomInfo.getScreenType());
        requestParam.put("closeLike",wxLiveRoomInfo.getCloseLike());
        requestParam.put("closeGoods",wxLiveRoomInfo.getCloseGoods());
        requestParam.put("closeComment",wxLiveRoomInfo.getCloseComment());
        requestParam.put("closeReplay",wxLiveRoomInfo.getCloseReplay());
        requestParam.put("closeShare",wxLiveRoomInfo.getCloseShare());
        requestParam.put("closeKf",wxLiveRoomInfo.getCloseKf());
        return signAndSend(wxLiveRoomInfo, requestParam,new TypeReference<WxServerResponse<Void>>() {});

    }
    /**
     * 获取直播间列表接口
     * @return
     */
    public WxServerResponse<List<RoomDetailResponse>> pageLiveRoom(PageLiveRoomInfo wxLiveRoomInfo){
        Map<String, Object> requestParam = new HashMap<>(16);
        requestParam.put("start",wxLiveRoomInfo.getStart());
        requestParam.put("limit", wxLiveRoomInfo.getLimit());

        return signAndSend(wxLiveRoomInfo, requestParam,new TypeReference<WxServerResponse<List<RoomDetailResponse>>>() {});

    }

    /**
     * 4.直播间导入商品
     * 调用接口往指定直播间导入已入库的商品
     * @return
     */
    public WxServerResponse<Void> addLiveRoomProds(WxRoomProdInfo wxRoomProdInfo){
        Map<String, Object> requestParam = new HashMap<>(16);
        requestParam.put("ids",wxRoomProdInfo.getIds());
        requestParam.put("roomId",wxRoomProdInfo.getRoomId());
        return signAndSend(wxRoomProdInfo, requestParam,new TypeReference<WxServerResponse<Void>>() {});
    }

    /**
     * 查询直播间回放
     * @return
     */
    public WxServerResponse<PageParam<RoomResponse>> pagePlayBackByRoomId(PageLiveRoomInfo wxRoomPlayBackInfo){
        Map<String, Object> requestParam = new HashMap<>(16);
        requestParam.put("action","get_replay");
        requestParam.put("room_id",wxRoomPlayBackInfo.getRoomId());
        requestParam.put("start",wxRoomPlayBackInfo.getStart());
        requestParam.put("limit",wxRoomPlayBackInfo.getLimit());
        return signAndSend(wxRoomPlayBackInfo, requestParam,new TypeReference<WxServerResponse<PageParam<RoomResponse>>>() {});
    }

//    /**
//     * 发送请求，获取响应结果
//     * @param requestParam
//     * @return
//     */
//    private <T> ServerResponse<T> signAndSend(WxInterfaceInfo creditInfo, Map<String, Object> requestParam, TypeReference<ServerResponse<T>> typeReference) {
////        log.info(Json.toJsonString(requestParam));
//        String uploadMediaUrl = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
//        uploadMediaUrl = uploadMediaUrl.replace("ACCESS_TOKEN", creditInfo.getAccessToken()).replace("TYPE", creditInfo.getType());
//        System.out.println(uploadMediaUrl);
//        String body = HttpUtil.createPost(uploadMediaUrl)
//                .contentType("multipart/form-data;boundary=----WebKitFormBoundaryLFoM36YJSszHqhMR")
//                .form(Json.toJsonString(requestParam))
//                .execute().body();
////        String body = HttpUtil.createPost(wxImgUrl + creditInfo.getRequestUrl()).form(requestParam).contentType("application/x-www-form-urlencoded").execute().body();
//        log.info(body);
//        return JSON.parseObject(body, typeReference);
//    }


    /**
     * 商品添加并提审
     * 商品添加到微信并且提交审核的接口
     */
    public GoodsInfoRespParam prodAddVerify(GoodsInfoReqParam goodsInfo){
        Map<String, Object> requestParam = new HashMap<>(16);
        requestParam.put("goodsInfo",goodsInfo);
        GoodsInfoRespParam goodsInfoRespParam = prodSignAndSend(goodsInfo, requestParam, new TypeReference<GoodsInfoRespParam>() {});
        System.out.println("goodsInfo: " + goodsInfoRespParam);
        if (!Objects.equals(goodsInfoRespParam.getErrcode(),0) ) {
            // 提交审核失败
            throw new YamiShopBindException("yami.examine.fail");
        }
        return goodsInfoRespParam;
    }

//    /**
//     * 撤回审核
//     * 可撤回直播商品的提审申请，消耗的提审次数不返还
//     */
//    public GoodsInfoRespParam prodCancelVerify(GoodsReqInfo goodsInfo){
//        Map<String, Object> requestParam = new HashMap<>(16);
//        requestParam.put("auditId",goodsInfo.getAuditId());
//        requestParam.put("goodsId",goodsInfo.getGoodsId());
//        goodsInfo.setRequestUrl("/resetaudit?access_token=");
//        GoodsInfoRespParam goodsRespInfo = prodSignAndSend(goodsInfo, requestParam, new TypeReference<GoodsInfoRespParam>() {});
//        if (!Objects.equals(goodsRespInfo.getErrcode(),0) ) {
//            throw new YamiShopBindExcetion("撤回审核失败");
//        }
//        return goodsRespInfo;
//    }

//    /**
//     * 重新提交审核
//     * 调用此接口可以对已撤回提审的商品再次发起提审申请
//     * 需要更新 "auditId": 525022184
//     */
//    public GoodsInfoRespParam prodAgainVerify(GoodsReqInfo goodsInfo){
//        Map<String, Object> requestParam = new HashMap<>(16);
//        requestParam.put("goodsId",goodsInfo.getGoodsId());
//        goodsInfo.setRequestUrl("/audit?access_token=");
//        GoodsInfoRespParam goodsRespInfo = prodSignAndSend(goodsInfo, requestParam, new TypeReference<GoodsInfoRespParam>() {});
//        if (!Objects.equals(goodsRespInfo.getErrcode(),0) ) {
//            throw new YamiShopBindExcetion("重新提交审核失败");
//        }
//        return goodsRespInfo;
//    }

    /**
     * 调用此接口，可删除【小程序直播】商品库中的商品，删除后直播间上架的该商品也将被同步删除，不可恢复
     * 删除商品
     * {
     *     "errcode": 0,
     * }
     */
    public WxLiveProdResponse<Void> prodDelete(GoodsReqInfo goodsInfo){
        Map<String, Object> requestParam = new HashMap<>(16);
        requestParam.put("goodsId",goodsInfo.getGoodsId());
        goodsInfo.setRequestUrl("/wxaapi/broadcast/goods/delete?access_token=");
        return liveProdSignAndSend(goodsInfo, requestParam, new TypeReference<WxLiveProdResponse<Void>>() {});
    }

    /**
     * 调用此接口可以更新商品信息，审核通过的商品仅允许更新价格类型与价格，审核中的商品不允许更新，
     * 未审核的商品允许更新所有字段， 只传入需要更新的字段
     * 更新商品
     * {
     *     "errcode": 0,
     * }
     */
    public WxLiveProdResponse<Void> prodUpdate(UpdateGoodsInfoReqParam goodsInfo){
        Map<String, Object> requestParam = new HashMap<>(16);
        requestParam.put("goodsInfo",goodsInfo);
        return liveProdSignAndSend(goodsInfo, requestParam, new TypeReference<WxLiveProdResponse<Void>>() {});
    }

    /**
     * 获取商品状态 调用此接口可获取商品的信息与审核状态
     * {
     *     "errcode": 0,
     * }
     */
    public WxLiveProdResponse<List<GoodsInfoRespParam>> prodGetStatus(GoodsReqInfo goodsInfo){
        Map<String, Object> requestParam = new HashMap<>(16);
        if (CollectionUtils.isEmpty(goodsInfo.getGoodsIds())) {
            List<Long> list = new ArrayList<>();
            list.add(goodsInfo.getGoodsId());
            requestParam.put("goods_ids", list.toArray());
        } else {
            requestParam.put("goods_ids", goodsInfo.getGoodsIds().toArray());
        }
        goodsInfo.setRequestUrl("/wxa/business/getgoodswarehouse?access_token=");
        return liveProdSignAndSend(goodsInfo, requestParam, new TypeReference<WxLiveProdResponse<List<GoodsInfoRespParam>>>() {});
    }

    /**
     * 获取商品列表 调用此接口可获取商品的信息与审核状态
     * 调用此接口可获取商品列表
     * {
     *     "errcode": 0,
     * }
     */
    public GoodsListRespParam prodList(GoodsListReqParam goodsInfo){
        Map<String, Object> requestParam = new HashMap<>(16);
        requestParam.put("offset", goodsInfo.getOffset());
        requestParam.put("limit", goodsInfo.getLimit());
        requestParam.put("status", goodsInfo.getStatus());
        GoodsListRespParam goodsRespInfo = prodSignAndSend(goodsInfo, requestParam, new TypeReference<GoodsListRespParam>() {});
        if (!Objects.equals(goodsRespInfo.getErrcode(),0) ) {
            // 获取商品列表失败
            throw new YamiShopBindException("yami.get.goods");
        }
        return goodsRespInfo;
    }

    /**
     *  微信请求-商品管理请求
     *
     */
    private <T> T prodSignAndSend(WxInterfaceInfo wxInterfaceInfo, Map<String, Object> requestParam, TypeReference<T> typeReference) {
        log.info(Json.toJsonString(requestParam));
        String reqUrl = goodsUrl +  wxInterfaceInfo.getRequestUrl() + wxInterfaceInfo.getAccessToken();
        if (wxInterfaceInfo.getRequestUrl().contains(GET_GOODS_WARE_HOUSE)) {
            reqUrl = goodsStatusUrl +  wxInterfaceInfo.getRequestUrl() + wxInterfaceInfo.getAccessToken();
        }
        if (wxInterfaceInfo.getRequestUrl().contains(GET_APPROVED)) {
            reqUrl = goodsListUrl.replace("ACCESS_TOKEN",wxInterfaceInfo.getAccessToken());
        }
        log.info("url: " + reqUrl);
        String body = HttpUtil.createPost(reqUrl)
                .body(Json.toJsonString(requestParam))
                .contentType("application/json")
                .execute().body();
        log.info(body);
        if (StringUtils.contains(body,ERR_MSG)) {
            String message = I18nMessage.getMessage("yami.request.error");
            throw new YamiShopBindException(message+body);
        }
        return JSON.parseObject(body,typeReference);
    }

    /**
     *  微信请求-统一直播间接口处理
     */
    private <T> WxServerResponse<T> signAndSend(WxInterfaceInfo wxInterfaceInfo, Map<String, Object> requestParam, TypeReference<WxServerResponse<T>> typeReference) {
        log.info(Json.toJsonString(requestParam));
        String str = liveUrl +  wxInterfaceInfo.getRequestUrl() + wxInterfaceInfo.getAccessToken();
        System.out.println("str: " + str);
        String body = HttpUtil.createPost(str)
                .body(Json.toJsonString(requestParam))
                .contentType("application/json")
                .execute().body();
        log.info(body);
        return JSON.parseObject(body, typeReference);
    }

    /**
     *  微信请求-统一直播商品处理
     */
    private <T> WxLiveProdResponse<T> liveProdSignAndSend(WxInterfaceInfo wxInterfaceInfo, Map<String, Object> requestParam, TypeReference<WxLiveProdResponse<T>> typeReference) {
        log.info(Json.toJsonString(requestParam));
        String str = liveUrl +  wxInterfaceInfo.getRequestUrl() + wxInterfaceInfo.getAccessToken();
        System.out.println("str: " + str);
        String body;
        try {
            body = HttpUtil.createPost(str)
                    .contentType("application/json")
                    .body(Json.toJsonString(requestParam))
                    .execute().body();
        }catch (Exception e){
            // 远程微信连接异常！
            throw new YamiShopBindException("yami.network.exception");
        }
        log.info(body);
        return JSON.parseObject(body, typeReference);
    }

}
