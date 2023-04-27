package com.chatchatabc.parking.web.common.application.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

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
                                // TODO: Put details on application properties
                                .title("Parking Swagger API")
                                .description("Parking Swagger API")
                                .version("1.0.0")
                );
    }
}
