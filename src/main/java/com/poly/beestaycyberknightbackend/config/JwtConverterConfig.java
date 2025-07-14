package com.poly.beestaycyberknightbackend.config;

import com.poly.beestaycyberknightbackend.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConverterConfig {

    @Bean
    public CustomJwtAuthenticationConverter customJwtAuthenticationConverter(UserService userService) {
        return new CustomJwtAuthenticationConverter(userService);
    }
}
