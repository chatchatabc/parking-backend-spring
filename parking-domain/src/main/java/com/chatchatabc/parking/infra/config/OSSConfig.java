package com.chatchatabc.parking.infra.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OSSConfig {
    @Value("${aliyun.access-id}")
    private String accessKeyId;
    @Value("${aliyun.access-secret}")
    private String accessKeySecret;
    @Value("${aliyun.oss.endpoint}")
    private String ossEndpoint;

    @Bean
    public OSS ossClient() {
        return new OSSClientBuilder().build(ossEndpoint, accessKeyId, accessKeySecret);
    }
}
