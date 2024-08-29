package me.flyray.bsin.server.impl;

import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.common.error.WxErrorException;
import me.flyray.bsin.facade.service.WxService;
import me.flyray.bsin.server.common.ServerResponseEntity;
import me.flyray.bsin.server.common.exception.BusinessException;
import me.flyray.bsin.server.config.WxConfig;
import me.flyray.bsin.server.utils.RespBodyHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

public class WxServiceImpl implements WxService {
    @Autowired
    private WxConfig wxConfig;

    @Override
    public Map<String, Object> createJsapiSignature(Map<String, Object> requestMap) {
        String url = (String) requestMap.get("url");
        WxJsapiSignature jsapiSignature;
        try {
            jsapiSignature = wxConfig.createJsapiSignature(url);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("appId", jsapiSignature.getAppId());
            hashMap.put("nonceStr", jsapiSignature.getNonceStr());
            hashMap.put("timestamp", String.valueOf(jsapiSignature.getTimestamp()));
            hashMap.put("url", jsapiSignature.getUrl());
            hashMap.put("signature", jsapiSignature.getSignature());
            return RespBodyHandler.setRespBodyDto(ServerResponseEntity.success(hashMap));
        } catch (Exception e) {
            throw new BusinessException(e.getMessage());
        }
    }
}
