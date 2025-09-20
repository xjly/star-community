package com.example.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@ConfigurationProperties(prefix = "weixin.miniapp")
@Data
@Component
public class WeChatPropertites {
    private String appId;
    private String appSecret;
}
