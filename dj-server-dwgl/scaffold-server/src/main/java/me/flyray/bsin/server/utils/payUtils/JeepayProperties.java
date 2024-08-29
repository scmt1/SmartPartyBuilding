package me.flyray.bsin.server.utils.payUtils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "jeepay")
public class JeepayProperties {
    /**
     * 商户号
     */
    private String mchNo;
    /**
     * appid
     */
    private String appId;
    /**
     * 秘钥
     */
    private String apiKey;
    /**
     * 通知地址
     */
    private String notifyUrl;
    /**
     * 返回地址
     */
    private String returnUrl;

    /**
     * 接口地址
     */
    private String jeepayURL;

    /**
     * 查询接口地址
     */
    private String queryURL;


    /**
     * 微信
     */
    private String jeepayURLWeiXin;

}
