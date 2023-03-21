package com.example.checkchallenge.config;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;

@Configuration
public class AppConfig {

    @Bean
    ConcurrentHashMap<String, Authentication> refreshTokenData() {
        return new ConcurrentHashMap<>();
    }
}

