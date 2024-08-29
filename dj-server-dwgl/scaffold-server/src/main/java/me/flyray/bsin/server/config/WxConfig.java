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

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.nacos.client.utils.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.util.RandomUtils;
import me.chanjar.weixin.common.util.crypto.SHA1;
import me.chanjar.weixin.mp.api.WxMpService;
import me.flyray.bsin.server.common.bean.WxMp;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.utils.RedisUtils;
import me.flyray.bsin.server.utils.payUtils.WxProperties;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 通过微信配置获取微信的支付信息，登陆信息等
 *
 * @author LGH
 */
@Component
@AllArgsConstructor
@Slf4j
public class WxConfig {

    public static final String APPID = "wx510b334198e3b687";
    public static final String SECRET = "a064d2be20e9b5a93291022486aa07e8";


    @Autowired
    private WxProperties wxProperties;


    @Autowired(required = true)
    private RedisUtils redisUtils;

    public WxMpService getWxMpService() {
        WxMp wxMp = new WxMp();
        wxMp.setAppId(APPID);
        wxMp.setSecret(SECRET);
        WxMpInRedisConfigStorage wxMaInRedisConfig = new WxMpInRedisConfigStorage(wxMp);
        WxMpServiceClusterImpl service = new WxMpServiceClusterImpl();
        service.setWxMpConfigStorage(wxMaInRedisConfig);
        return service;
    }

    public WxJsapiSignature createJsapiSignature(String url) {
        long timestamp = System.currentTimeMillis() / 1000L;
        String randomStr = RandomUtils.getRandomStr();
        String jsapiTicket = this.getJsapiTicket();

        String s = "jsapi_ticket=" + jsapiTicket + "&noncestr=" + randomStr + "&timestamp=" + timestamp+ "&url=" + url;
        String signature = SHA1.genWithAmple(s);
        WxJsapiSignature jsapiSignature = new WxJsapiSignature();
        jsapiSignature.setAppId(APPID);
        jsapiSignature.setTimestamp(timestamp);
        jsapiSignature.setNonceStr(randomStr);
        jsapiSignature.setUrl(url);
        jsapiSignature.setSignature(signature);
        return jsapiSignature;
    }

    private String getJsapiTicket() {
//        String ticket_old = (String) redisUtils.get("ticket:" + APPID);
//        if(StringUtils.isNotEmpty(ticket_old)) {
//            return ticket_old;
//        }
        String token = getWxgAccessToken();
        String requestUrl = StrUtil.format(wxProperties.getApiUrl() + "/cgi-bin/ticket/getticket?access_token={}&type=jsapi", token);
        String returnMsg = HttpUtil.get(requestUrl);
        cn.hutool.json.JSONObject responseJsonObject = JSONUtil.parseObj(returnMsg);
        log.info("获取微信ticket：{}", responseJsonObject);
        if (ObjectUtil.isNull(responseJsonObject)) {
            throw new BusinessException("获取ticket失败！");
        }
        String ticket = responseJsonObject.getStr("ticket");
        redisUtils.set("ticket:" + APPID, ticket, 7200, TimeUnit.SECONDS);
        return ticket;
    }




    public String getWxgAccessToken() {
//        String token = (String) redisUtils.get("access_token:" + APPID);
//        if(StringUtils.isNotEmpty(token)) {
//            return token;
//        }
        // 服务号的appid以及秘钥
        String requestUrl = wxProperties.getApiUrl() + "/cgi-bin/stable_token";
        JSONObject reqMap = new JSONObject();
        reqMap.put("appid", APPID);
        reqMap.put("secret", SECRET);
        reqMap.put("grant_type", "client_credential");
        String returnMsg = HttpUtil.post(requestUrl, reqMap.toJSONString());
        cn.hutool.json.JSONObject responseJsonObject = JSONUtil.parseObj(returnMsg);
        log.info("获取微信token：{}", responseJsonObject);
        if (ObjectUtil.isNull(responseJsonObject)) {
            throw new BusinessException("获取token失败！");
        }
        String accessToken = responseJsonObject.getStr("access_token");
        redisUtils.set("access_token:" + APPID, accessToken, 7200, TimeUnit.SECONDS);
        return accessToken;
    }


    public Boolean sendWxgMessage(String openid, String templateId, JSONObject data, String pageUrl) {
        //此处的参数key,需要对照模板中的key来设置
//        data = new JSONObject();
//        data.put("first", getValue("尊敬的用户，您的党费还未缴纳，请尽快缴纳党费"));
//        data.put("keyword1", getValue("何兴海"));
//        data.put("keyword2", getValue("中共泸州市直属工作委员会机关支部委员会"));
//        data.put("keyword3", getValue("2024-08月份"));
//        data.put("keyword4", getValue("12.5"));
//        data.put("remark", getValue("点击查看详情"));


        String wxgAccessToken = getWxgAccessToken();
        JSONObject jsonData = new JSONObject();
        jsonData.put("touser", openid);
        jsonData.put("template_id", templateId);
        jsonData.put("url", pageUrl);
        jsonData.put("data", data);
        // 发送消息
        String returnMsg = HttpUtil.post(StrUtil.format(wxProperties.getApiUrl() + "/cgi-bin/message/template/send?access_token={}", wxgAccessToken), jsonData.toJSONString());
        cn.hutool.json.JSONObject jsonObject = JSONUtil.parseObj(returnMsg);
        log.info("发送微信模板消息：{}", jsonObject);
        String errmsg = jsonObject.getStr("errmsg");
        if (!StrUtil.equals("ok", errmsg)) {
            return false;
        }
        return true;
    }

    public JSONObject getValue(String value) {
        // TODO Auto-generated method stub
        JSONObject json = new JSONObject();
        json.put("value", value);
        json.put("color", "#173177");
        return json;
    }


    public WxOAuth2AccessToken getWxUserAccessToken(String code) {
        // 服务号的appid以及秘钥
        String appid = APPID;
        String Wxgsecret = SECRET;
        String requestUrl = StrUtil.format(wxProperties.getApiUrl() + "/sns/oauth2/access_token?appid={}&secret={}&code={}&grant_type=authorization_code", appid, Wxgsecret, code);
        String returnMsg = HttpUtil.get(requestUrl);
        log.info("请求用户信息：{}", returnMsg);
        return WxOAuth2AccessToken.fromJson(returnMsg);
    }

}
