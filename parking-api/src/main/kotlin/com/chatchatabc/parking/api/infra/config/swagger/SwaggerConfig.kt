package com.chatchatabc.parking.api.infra.config.swagger

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    /**
     * Custom OpenAPI configuration
     */
    @Bean
    fun customOpenAPI(): OpenAPI {
        return OpenAPI().components(Components())
            .info(
                Info().title("Parking Management API")
                    .description("Parking Management API")
                    .version("1.0.0")
            )
    }
}