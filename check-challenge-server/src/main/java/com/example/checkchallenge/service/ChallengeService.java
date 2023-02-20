package com.example.checkchallenge.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.checkchallenge.model.Challenge;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class ChallengeService {
	
	private final WebClient webClient;
	
	public Flux<Challenge> getChallenges(String username) {
        return webClient
        		.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .bodyToFlux(Challenge.class);
    }

}
