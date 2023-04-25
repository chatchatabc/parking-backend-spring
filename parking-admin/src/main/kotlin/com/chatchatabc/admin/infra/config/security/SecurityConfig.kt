package com.chatchatabc.admin.infra.config.security

import jakarta.servlet.SessionTrackingMode
import jakarta.servlet.http.HttpSessionEvent
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.session.HttpSessionEventPublisher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.util.*

@Configuration
@EnableWebSecurity
class SecurityConfig {
    /**
     * Configure security rules
     */
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .cors().and().csrf().disable()
            .authorizeHttpRequests {
                it.requestMatchers("/api/auth/**").permitAll()

                // All routes must have admin role
                it.requestMatchers("/api/**").hasAnyRole("ADMIN")

                // Allow route to Swagger UI
                it.requestMatchers("/api/swagger-ui/**").permitAll()
                it.requestMatchers("/api/v3/api-docs/**").permitAll()

                it.anyRequest().authenticated()
            }
            .sessionManagement { session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .build()
    }

    /**
     * Configure CORS
     */
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        // TODO: Add this to application.properties
        configuration.allowedOrigins = listOf("http://localhost:3000")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")
        configuration.allowedHeaders = listOf("authorization", "content-type", "x-auth-token", "X-Access-Token")
        // Allow client to write to header
        configuration.exposedHeaders = listOf("x-auth-token", "X-Access-Token")
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

    @Bean
    fun httpSessionEventPublisher(): HttpSessionEventPublisher {
        return object : HttpSessionEventPublisher() {
            override fun sessionCreated(event: HttpSessionEvent) {
                // Set session tracking mode to COOKIE
                event.session.servletContext.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE))
                super.sessionCreated(event)
            }
        }
    }

    @Bean
    protected fun configure(http: HttpSecurity) {
        http
            .sessionManagement()
            .sessionFixation()
            .migrateSession()
            .maximumSessions(1)
            .maxSessionsPreventsLogin(true)
            // TODO: Change URL and add to application.properties
            .expiredUrl("/login?expired")
            .and()
            .invalidSessionUrl("/login?invalid")
    }

    /**
     * Authentication manager bean definition.
     */
    @Bean
    fun authenticationManager(configuration: AuthenticationConfiguration): AuthenticationManager {
        return configuration.authenticationManager
    }

    /**
     * Password encoder bean definition.
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}