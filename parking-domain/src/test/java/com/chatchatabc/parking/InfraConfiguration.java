package com.chatchatabc.parking;

import com.aliyun.oss.OSS;
import com.chatchatabc.parking.impl.infra.oss.OSSMockClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class InfraConfiguration {
    @Bean
    public OSS ossMockClient() {
        return new OSSMockClient();
    }
}
