/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package me.flyray.bsin.server.config;

import cn.hutool.http.HttpUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.chanjar.weixin.common.bean.WxAccessToken;
import me.chanjar.weixin.common.enums.TicketType;
import me.chanjar.weixin.common.enums.WxType;
import me.chanjar.weixin.common.error.WxError;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.util.http.SimpleGetRequestExecutor;
import me.chanjar.weixin.mp.api.impl.WxMpServiceHttpClientImpl;
import me.flyray.bsin.server.common.exception.BusinessException;

import java.util.concurrent.TimeUnit;

import static me.chanjar.weixin.mp.enums.WxMpApiUrl.Other.GET_ACCESS_TOKEN_URL;
import static me.chanjar.weixin.mp.enums.WxMpApiUrl.Other.GET_TICKET_URL;

/**
 * WxMpServiceImpl 在集群模式获取accessToken、Ticket的方式
 * @author LGH
 */
public class WxMpServiceClusterImpl extends WxMpServiceHttpClientImpl {

//    private static final JsonParser JSON_PARSER = new JsonParser();

//    private static final String REDISSON_LOCK_PREFIX = "redisson_lock:";
//
//    private RedissonClient redissonClient;
//
//    public void setRedissonClient(RedissonClient redissonClient) {
//        this.redissonClient = redissonClient;
//    }
//
//    @Override
//    public String getAccessToken(boolean forceRefresh) throws WxErrorException {
//        if (!this.getWxMpConfigStorage().isAccessTokenExpired() && !forceRefresh) {
//            return this.getWxMpConfigStorage().getAccessToken();
//        }
//
//        RLock rLock = redissonClient.getLock(REDISSON_LOCK_PREFIX + ":WxMpServiceCluster:getAccessToken");
//
//        try {
//            boolean doingUpdateAccessToken;
//            try {
//                doingUpdateAccessToken = rLock.tryLock(5, 60, TimeUnit.SECONDS);
//            } catch (InterruptedException e) {
//                return this.getWxMpConfigStorage().getAccessToken();
//            }
//
//            if (!doingUpdateAccessToken) {
//                // 服务器繁忙，请稍后再试
//                throw new BusinessException("服务器繁忙，请稍后再试");
//            }
//
//            if (!this.getWxMpConfigStorage().isAccessTokenExpired()) {
//                return this.getWxMpConfigStorage().getAccessToken();
//            }
//            String url = String.format(GET_ACCESS_TOKEN_URL.getUrl(this.getWxMpConfigStorage()), this.getWxMpConfigStorage().getAppId(), this.getWxMpConfigStorage().getSecret());
//            String resultContent = HttpUtil.get(url);
//
//            WxError error = WxError.fromJson(resultContent, WxType.MP);
//            if (error.getErrorCode() != 0) {
//                throw new WxErrorException(error);
//            }
//            WxAccessToken accessToken = WxAccessToken.fromJson(resultContent);
//            this.getWxMpConfigStorage().updateAccessToken(accessToken.getAccessToken(), accessToken.getExpiresIn());
//            return this.getWxMpConfigStorage().getAccessToken();
//
//        } finally {
//            rLock.unlock();
//        }
//    }
//
//
//    @Override
//    public String getTicket(TicketType type, boolean forceRefresh) throws WxErrorException {
//
//        if (!this.getWxMpConfigStorage().isTicketExpired(type) && !forceRefresh) {
//            this.getWxMpConfigStorage().getTicket(type);
//        }
//
//        RLock rLock = redissonClient.getLock(REDISSON_LOCK_PREFIX + ":WxMpServiceCluster:getTicket");
//        try {
//            boolean doingUpdateTicket;
//            try {
//                doingUpdateTicket = rLock.tryLock(5, 60, TimeUnit.SECONDS);
//            } catch (InterruptedException e) {
//                return this.getWxMpConfigStorage().getTicket(type);
//            }
//
//            if (!doingUpdateTicket) {
//                // 网络繁忙，请稍后重试
//                throw new BusinessException("网络繁忙，请稍后重试");
//            }
//            if (!this.getWxMpConfigStorage().isTicketExpired(type)) {
//                this.getWxMpConfigStorage().getTicket(type);
//            }
//
//            String responseContent = execute(SimpleGetRequestExecutor.create(this),
//                    GET_TICKET_URL.getUrl(this.getWxMpConfigStorage()) + type.getCode(), null);
////            JsonObject tmpJsonObject = JSON_PARSER.parse(responseContent).getAsJsonObject()
//            JsonObject tmpJsonObject = JsonParser.parseString(responseContent).getAsJsonObject();
//            String jsapiTicket = tmpJsonObject.get("ticket").getAsString();
//            int expiresInSeconds = tmpJsonObject.get("expires_in").getAsInt();
//            this.getWxMpConfigStorage().updateTicket(type, jsapiTicket, expiresInSeconds);
//        } finally {
//            rLock.unlock();
//        }
//
//        return this.getWxMpConfigStorage().getTicket(type);
//    }

}
