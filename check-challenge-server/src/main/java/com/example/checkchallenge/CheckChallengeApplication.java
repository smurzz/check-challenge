package com.example.checkchallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication // (exclude={MongoAutoConfiguration.class})
@ConfigurationPropertiesScan
@EnableScheduling
public class CheckChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CheckChallengeApplication.class, args);
    }

}
