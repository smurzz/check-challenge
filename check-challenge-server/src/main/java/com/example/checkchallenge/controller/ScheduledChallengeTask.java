package com.example.checkchallenge.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.checkchallenge.repository.ChallengeRepository;
import com.example.checkchallenge.service.ChallengeService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ScheduledChallengeTask {
	
	@Value("${github.username}")
	private String username;
	
	private final ChallengeService challengeService;
    private final ChallengeRepository challengeRepository;

    @Scheduled(fixedRate = 3600000) // an hour
    public void updateChallengesRepositories() {
    	 challengeService
	    	 .getChallenges(username)
	         .flatMap(challenge -> challengeRepository.existsById(challenge.getId())
	        		 .flatMap(exists -> exists ? Mono.empty() : challengeRepository.save(challenge))
	         )
	         .subscribe();	
    }
}