package com.chatchatabc.parking.web.common.application.config.rest;

import com.chatchatabc.parking.web.common.application.rest.service.GpsRestService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class AzliotRestConfig {
    private static final String AZLIOT_BASE_URL = "http://api-tf.azliot.com";
    public static String AZLIOT_TOKEN = null;

    // Values from .env
    @Value("${azliot.api.tf-key}")
    private String tfKey;

    @Bean
    public GpsRestService gpsRestServiceConfig() {
        // Setup Azliot Rest Config
        WebClient azliotClient = WebClient.builder()
                .baseUrl(AZLIOT_BASE_URL)
                .defaultHeader("tfKey", tfKey)

                // Load headers dynamically
                .filter((request, next) -> {
                    // Add Authorization header if AZLIOT_TOKEN is not null
                    if (AZLIOT_TOKEN != null) {
                        request = ClientRequest.from(request)
                                .header("Authorization", AZLIOT_TOKEN)
                                .build();
                    }
                    return next.exchange(request);
                })
                .build();
        HttpServiceProxyFactory azliotFactory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(azliotClient))
                .build();
        return azliotFactory.createClient(GpsRestService.class);
    }
}
