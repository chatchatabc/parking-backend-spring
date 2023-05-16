package com.chatchatabc.parking.web.common.application.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Value("${server.swagger.title}")
    private String title;
    @Value("${server.swagger.description}")
    private String description;
    @Value("${server.swagger.version}")
    private String version;

    /**
     * Swagger configuration
     *
     * @return OpenAPI
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().components(new Components())
                .info(
                        new Info()
                                .title(title)
                                .description(description)
                                .version(version)
                );
    }
}
