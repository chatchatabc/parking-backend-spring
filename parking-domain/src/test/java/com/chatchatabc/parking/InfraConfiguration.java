package com.chatchatabc.parking;

import com.aliyun.oss.OSS;
import com.chatchatabc.parking.impl.infra.oss.OSSMockClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;


@TestConfiguration
public class InfraConfiguration {
    @Bean
    public OSS ossMockClient() {
        return new OSSMockClient();
    }
}
