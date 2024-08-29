package me.flyray.bsin.server.utils.payUtils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "wx")
public class WxProperties {

    private String apiUrl;
}
