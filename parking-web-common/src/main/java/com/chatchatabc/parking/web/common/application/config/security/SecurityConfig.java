package com.chatchatabc.parking.web.common.application.config.security;

import com.chatchatabc.parking.web.common.application.config.security.filter.JwtRequestFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtRequestFilter jwtRequestFilter;
    private final DataSource dataSource;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter, DataSource dataSource) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.dataSource = dataSource;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    // Allow all requests to the authentication endpoint
                    auth.requestMatchers("/api/auth/**").permitAll();

                    // Allow routes to Swagger UI
                    // TODO: Add auth in the future
                    auth.requestMatchers("/api/swagger-ui/**").permitAll();
                    auth.requestMatchers("/api/v3/api-docs/**").permitAll();

                    // Allow public access to user avatar url
                    auth.requestMatchers("/api/user/avatar/**").permitAll();
                    // User routes must be authenticated
                    auth.requestMatchers("/api/user/**").authenticated();

                    // Allow public access for vehicle brand logos
                    auth.requestMatchers("/api/vehicle-brand/logo/**").permitAll();

                    // Expose Image Routes for Vehicles
                    auth.requestMatchers("/api/vehicle/image/**").permitAll();
                    // Routes for Vehicle
                    auth.requestMatchers("/api/vehicle/**").authenticated();

                    // Routes for Report
                    // TODO: Get reports concerning user
                    auth.requestMatchers("/api/report/create-report").authenticated();
                    auth.requestMatchers("/api/report/**").hasAnyRole("ADMIN", "ENFORCER");

                    // Routes for Jeepney
                    // TODO: Make specific routes for roles
                    auth.requestMatchers("/api/jeepney/**").authenticated();

                    // Routes for Routes
                    auth.requestMatchers("/api/route/go").permitAll();
                    auth.requestMatchers("/api/route/nodes-and-edges/**").permitAll();
                    auth.requestMatchers("/api/route").authenticated();
                    auth.requestMatchers("/api/route/last-updated-at/**").authenticated();
                    auth.requestMatchers("/api/route/**").hasAnyRole("ADMIN", "ENFORCER");
                    // TODO: Add routes for other controllers

                    // Permit Graphql
                    // TODO: Add auth in the future
                    auth.requestMatchers("/graphiql").permitAll();
                    auth.requestMatchers("/graphql").permitAll();

                    // Permit Actuator
                    // TODO: Add auth in the future
                    auth.requestMatchers("/actuator/**").permitAll();
                    auth.requestMatchers("/actuator/**").permitAll();

                    // TODO: Temporarily allow request to images
                    auth.requestMatchers("/api/parking-lot/image/**").permitAll();
                    auth.requestMatchers("/api/parking-lot/featured-image/**").permitAll();

                    auth.anyRequest().authenticated();
                })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Configure CORS
     *
     * @return the CORS configuration source
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        // TODO: Add some on application properties
        configuration.setAllowedOrigins(
                List.of(
                        "http://localhost:3000",
                        "http://127.0.0.1:5500",
                        "http://localhost:5500",
                        "https://davao-parking-admin.pages.dev",
                        "http://192.168.1.11:5180",
                        "https://admin-api.microservices.club",
                        "https://client-api.microservices.club"
                )
        );
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("authorization", "content-type", "x-auth-token", "X-Access-Token"));
        // Allow client to write to header
        configuration.setExposedHeaders(List.of("x-auth-token", "X-Access-Token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Configure the authentication manager
     *
     * @param authenticationConfiguration the authentication configuration
     * @return the authentication manager
     * @throws Exception if an error occurs
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

//    @Bean
//    public UserDetailsManager users(DataSource dataSource) {
//        return new JdbcUserDetailsManager(dataSource);
//    }

    /**
     * Configure the password encoder
     *
     * @return the password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        String idForEncode = "bcrypt";
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(idForEncode, new BCryptPasswordEncoder());
        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }
}
