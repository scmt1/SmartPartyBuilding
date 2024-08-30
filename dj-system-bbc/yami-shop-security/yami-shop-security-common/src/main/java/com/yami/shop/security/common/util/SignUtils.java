/*
 * Copyright (c) 2018-2999 广州市蓝海创新科技有限公司 All rights reserved.
 *
 * https://www.mall4j.com/
 *
 * 未经允许，不可做商业用途！
 *
 * 版权所有，侵权必究！
 */
package com.yami.shop.security.common.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.http.ContentType;
import com.yami.shop.bean.model.SysAccessKey;
import com.yami.shop.common.response.ServerResponseEntity;
import com.yami.shop.common.util.Json;
import com.yami.shop.common.wrapper.RequestWrapper;
import com.yami.shop.security.common.bo.SignResponse;
import com.yami.shop.service.SysAccessKeyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * 签名工具类
 * @author yami
 */
@Component
@Slf4j
public class SignUtils {

    public static final String GRANT_TYPE = "grant_type";

    public static final String GRANT_TYPE_VALUE = "sign";

    private static final long TEN_MINUTES = 1000 * 60 * 10;

    /**
     * 签名，通过签名机制，防止应用的请求参数被非法篡改，业务系统必须保证该值不被泄露。
     */
    public static final String SIGN = "sign";

    /**
     * 分配给应用的系统编号
     */
    public static final String APP_ID = "appId";

    /**
     * 分配给应用的系统密钥
     */
    public static final String APP_SECRET = "appSecret";

    /**
     * 请求的时间戳，接入系统的时间误差不能超过 10 分钟
     */
    public static final String TIMESTAMP = "timestamp";

    /**
     * 真正请求的数据
     */
    public static final String DATA = "data";

    @Autowired
    private SysAccessKeyService sysAccessKeyService;


    /**
     * 验签方法
     */
    public ServerResponseEntity<SignResponse> verify(HttpServletRequest req) throws IOException {
        // 这里的param map是包含sign, timestamp等信息的，如果是json形式那么data在下一层，如果不是就在同一层
        TreeMap<String, Object> paramMap;
        // data所在那一层的map数据
        Map<String, Object> dataMap;
        if (StrUtil.isNotBlank(req.getContentType()) && req.getContentType().contains(ContentType.JSON.getValue())) {
            RequestWrapper requestWrapper = new RequestWrapper(req);
            String body = requestWrapper.getBody();
            // 签名是有顺序的
            paramMap = Json.parseObject(body, TreeMap.class);
            dataMap = (Map<String, Object>) paramMap.get(DATA);
            requestWrapper.setBody(Json.toJsonString(dataMap));
            // 这里改写了请求将data里面的数据发送到controller
            req = requestWrapper;
        } else {
            paramMap = new TreeMap<>(req.getParameterMap());
            for (Map.Entry<String, Object> objectEntry : paramMap.entrySet()) {
                String[] list = (String[]) objectEntry.getValue();
                if (list.length == 1) {
                    paramMap.put(objectEntry.getKey(), list[0]);
                    continue;
                }
                Arrays.sort(list);
            }
            dataMap = paramMap;
        }
        ServerResponseEntity<SignResponse> verifyResponse = verify(paramMap);
        if (!verifyResponse.isSuccess()) {
            return verifyResponse;
        }
        SignResponse signResponse = verifyResponse.getData();
        signResponse.setReq(req);
        signResponse.setDataMap(dataMap);
        return ServerResponseEntity.success(signResponse);
    }

    /**
     * 校验参数，如果校验成功则返回data数据信息
     * @return
     */
    public ServerResponseEntity<SignResponse> verify(TreeMap<String, Object> paramMap) {
        SignResponse signResponse = new SignResponse();

        try {
            String appId = (String)paramMap.get(APP_ID);
            if (StrUtil.isBlank(appId)) {
                return ServerResponseEntity.showFailMsg("appid missing").setData(signResponse);
            }
            SysAccessKey sysAccessKey = sysAccessKeyService.getByAccessId(appId);
            if (sysAccessKey == null) {
                return ServerResponseEntity.showFailMsg("appid error").setData(signResponse);
            }
            // 签名
            String sign = (String)paramMap.get(SIGN);
            // 移除签名
            paramMap.remove(SIGN);
            // 移除appId
            paramMap.remove(APP_ID);
            // 用密钥来做签名
            paramMap.put(APP_SECRET, sysAccessKey.getAccessKey());
            signResponse.setAppSecret(sysAccessKey.getAccessKey());


            long currentTimeMillis = System.currentTimeMillis();
            Long timestamp = Long.valueOf(paramMap.get(TIMESTAMP).toString());
            // 签名时间大于十分钟，提示签名超时
            if (timestamp + TEN_MINUTES < currentTimeMillis) {
                return ServerResponseEntity.showFailMsg("The request time has exceeded ten minutes").setData(signResponse);
            }
            // 生成签名
            Digester sha256 = new Digester(DigestAlgorithm.SHA256);
            String sysSign = sha256.digestHex(Json.toJsonString(paramMap));

            // 进行验签
            if (!Objects.equals(sign, sysSign)) {
                return ServerResponseEntity.showFailMsg("sign error").setData(signResponse);
            }
        } catch (Exception e) {
            log.info("sign verify error : {}", e.getMessage());
            return ServerResponseEntity.showFailMsg("system error").setData(signResponse);
        }
        return ServerResponseEntity.success(signResponse);
    }
    public static String sign(String appSecret, Long timestamp, Object data) {

        TreeMap<String, Object> requestMap = new TreeMap<>();
        requestMap.put(TIMESTAMP, timestamp);
        requestMap.put(APP_SECRET, appSecret);
        requestMap.put(DATA, data);

        // 签名
        Digester sha256 = new Digester(DigestAlgorithm.SHA256);
        String sign = sha256.digestHex(Json.toJsonString(requestMap));
        // 请求数据添加签名和账号id
        requestMap.put("sign", sign);
        return sign;
    }

}
