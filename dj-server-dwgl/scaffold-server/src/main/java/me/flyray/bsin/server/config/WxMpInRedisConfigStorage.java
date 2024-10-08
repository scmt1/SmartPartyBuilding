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

import me.chanjar.weixin.common.bean.WxAccessToken;
import me.chanjar.weixin.common.enums.TicketType;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import me.flyray.bsin.server.annotation.RedisLock;
import me.flyray.bsin.server.common.bean.WxMp;
import me.flyray.bsin.server.utils.RedisUtil;

/**
 * 基于Redis的微信配置provider.
 *
 * 已加入分布式锁的实现
 *
 * @author LGH
 */
public class WxMpInRedisConfigStorage extends WxMpDefaultConfigImpl {

    private static final String ACCESS_TOKEN_KEY = "wxMp:access_token:";

    private String accessTokenKey;

    public WxMpInRedisConfigStorage(WxMp wxMp) {
        this.setAppId(wxMp.getAppId());
        this.setSecret(wxMp.getSecret());
    }

    /**
     * 每个公众号生成独有的存储key.
     */
    @Override
    public void setAppId(String appId) {
        super.setAppId(appId);
        this.accessTokenKey = ACCESS_TOKEN_KEY.concat(appId);
    }

    private String getTicketRedisKey(TicketType type) {
        return String.format("wx:ticket:key:%s:%s", this.appId, type.getCode());
    }

    @Override
    public String getAccessToken() {
        return RedisUtil.get(accessTokenKey);
    }

    @Override
    public boolean isAccessTokenExpired() {
        return !RedisUtil.hasKey(accessTokenKey);
    }

    @Override
    @RedisLock(lockName = "updateMpAccessToken")
    public void updateAccessToken(WxAccessToken accessToken) {
        updateAccessToken(accessToken.getAccessToken(), accessToken.getExpiresIn());
    }


    @Override
    @RedisLock(lockName = "updateMpAccessToken")
    public void updateAccessToken(String accessToken, int expiresInSeconds) {
        RedisUtil.set(accessTokenKey, accessToken, expiresInSeconds - 200);
    }

    @Override
    public void expireAccessToken() {
        RedisUtil.del(accessTokenKey);
    }

    @Override
    public String getTicket(TicketType type) {
        return RedisUtil.get(this.getTicketRedisKey(type));
    }

    @Override
    public boolean isTicketExpired(TicketType type) {
        return !RedisUtil.hasKey(this.getTicketRedisKey(type));
    }

    @Override
    @RedisLock(lockName = "updateMpJsapiTicket")
    public void updateTicket(TicketType type, String jsapiTicket, int expiresInSeconds) {
        RedisUtil.set(this.getTicketRedisKey(type), jsapiTicket, expiresInSeconds - 200);
    }

    @Override
    public void expireTicket(TicketType type) {
        RedisUtil.del(this.getTicketRedisKey(type));
    }

}
