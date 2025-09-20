package com.example.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data //lombok
@Component
@ConfigurationProperties(prefix = "tencent.cos")
public class TencentCosProperties {

    private String accessKey;
    private String secretKey;
    private String region;
    private String bucket;
    private String baseUrl;
}

