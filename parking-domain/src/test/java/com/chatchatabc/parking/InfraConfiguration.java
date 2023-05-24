package com.chatchatabc.parking;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.chatchatabc.parking.impl.infra.oss.OSSMockClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;


@TestConfiguration
public class InfraConfiguration {
    @Bean
    public OSS ossMockClient() {
        return new OSSMockClient();
    }

    @Bean
    @Primary
    @ConditionalOnProperty(name = "aliyun.access.key", havingValue = "5b4oc8pkxwrp9v2tknuiobb6")
    public OSS ossClient(@Value("${aliyun.oss.endpoint}") String endpoint,
                         @Value("${aliyun.access.key}") String accessKey,
                         @Value("${aliyun.access.secret}") String accessSecret) {
        return new OSSClientBuilder().build(endpoint, accessKey, accessSecret);
    }

}
