package me.flyray.bsin.server.domain;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 统一认证 配置属性
 *
 * @author Lion Li
 */
@Data
@Component
@ConfigurationProperties(prefix = "oneaccess")
public class OneaccessProperties {
    /**
     * appid
     */
    private String appZoneId;

    /**
     * 统一认证地址
     */
    private String oauthUrl;

    /**
     * AppSecret
     */
    private String appSecret;

    /**
     * grant_type
     */
    private String grantType;
}
