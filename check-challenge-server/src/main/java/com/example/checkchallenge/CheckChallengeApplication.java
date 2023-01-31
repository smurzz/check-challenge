package com.example.checkchallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication // (exclude={MongoAutoConfiguration.class})
@ConfigurationPropertiesScan
public class CheckChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CheckChallengeApplication.class, args);
    }

}
