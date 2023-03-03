package com.example.checkchallenge.config;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    ConcurrentHashMap<String, String> refreshTokenData() {
        return new ConcurrentHashMap<>();
    }
}

