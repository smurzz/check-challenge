package com.example.checkchallenge.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
//@Profile("cors")
public class WebConfig {

    private static final Logger LOG = LoggerFactory.getLogger(WebConfig.class);

    @Bean
    WebClient webClient() {

        return WebClient.builder()
                .filters(exchangeFilterFunctions -> {
                    exchangeFilterFunctions.add(logRequest());
                    exchangeFilterFunctions.add(logResponse());
                })
                .build();
    }

    private ExchangeFilterFunction logRequest() {
        return (clientRequest, next) -> {
            LOG.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            LOG.info("--- Http Headers: ---");
            clientRequest.headers().forEach(this::logHeader);
            LOG.info("--- Http Cookies: ---");
            clientRequest.cookies().forEach(this::logHeader);
            return next.exchange(clientRequest);
        };
    }

    private ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            LOG.info("Response: {}", clientResponse.statusCode());
            clientResponse.headers().asHttpHeaders()
                    .forEach((name, values) -> values.forEach(value -> LOG.info("{}={}", name, value)));
            return Mono.just(clientResponse);
        });
    }

    private void logHeader(String name, List<String> values) {
        values.forEach(value -> LOG.info("{}={}", name, value));
    }

}
