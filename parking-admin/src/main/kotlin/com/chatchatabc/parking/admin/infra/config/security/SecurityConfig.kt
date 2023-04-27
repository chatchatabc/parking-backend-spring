package com.chatchatabc.parking.admin.infra.config.security

import com.chatchatabc.parking.admin.infra.config.security.filter.CsrfTokenResponseHeaderBindingFilter
import com.chatchatabc.parking.admin.infra.config.security.filter.SessionRequestFilter
import jakarta.servlet.SessionTrackingMode
import jakarta.servlet.http.HttpSessionEvent
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.security.web.csrf.CsrfFilter
import org.springframework.security.web.session.HttpSessionEventPublisher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.util.*

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val sessionRequestFilter: SessionRequestFilter,
    private val csrfTokenResponseHeaderBindingFilter: CsrfTokenResponseHeaderBindingFilter
) {
    /**
     * Configure security rules
     */
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
                // Enable cors
                .cors().and()
                // Enable CSRF
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
                .authorizeHttpRequests {
                    // Allow certain post requests
                    it.requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                    // Allow all get requests
                    it.requestMatchers("/api/auth/**").permitAll()

                    // User Path
                    it.requestMatchers("/api/user/**").hasAnyRole("ADMIN")

                    // Allow route to Swagger UI
                    it.requestMatchers("/api/swagger-ui/**").permitAll()
                    it.requestMatchers("/api/v3/api-docs/**").permitAll()

                    // All routes must have admin role
                    it.requestMatchers("/api/**").hasAnyRole("ADMIN")

                    // Permit actuator
                    it.requestMatchers("/actuator/**").permitAll()

                    it.anyRequest().authenticated()
                }
                .sessionManagement { session ->
                    session
                            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                            .sessionFixation().migrateSession()
                            .maximumSessions(1)
                            .maxSessionsPreventsLogin(true)
                            // TODO: Change URL and add to application.properties
                            .expiredUrl("/login?expired")
                            .and()
                            .invalidSessionUrl("/login?invalid")
                }
                .addFilterBefore(csrfTokenResponseHeaderBindingFilter, CsrfFilter::class.java)
                .addFilterBefore(sessionRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
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