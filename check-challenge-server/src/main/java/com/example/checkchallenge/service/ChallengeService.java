package com.example.checkchallenge.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.checkchallenge.model.Challenge;
import com.example.checkchallenge.repository.ChallengeRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ChallengeService {
	
	@Value("${github.username}")
	private String username;
	
	private final WebClient webClient;
	
	public Flux<Challenge> getChallenges() {
        return webClient
        		.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .bodyToFlux(Challenge.class);
    }

}
