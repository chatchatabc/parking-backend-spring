package com.chatchatabc.parking.infra.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfigDomain {
    /**
     * Configure Password Encoder
     *
     * @return the password encoder
     */
    @Bean
    public PasswordEncoder passwordEncoderDomain() {
        return new BCryptPasswordEncoder();
    }
}
