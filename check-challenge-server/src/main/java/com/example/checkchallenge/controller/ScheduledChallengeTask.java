package com.example.checkchallenge.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.checkchallenge.repository.ChallengeRepository;
import com.example.checkchallenge.service.ChallengeService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ScheduledChallengeTask {
	
	private final ChallengeService challengeService;
    private final ChallengeRepository challengeRepository;

    @Scheduled(fixedRate = 3600000) // an hour
    public void updateChallengesRepositories() {
    	 challengeService
	    	 .getChallenges()
	         .flatMap(challenge -> challengeRepository.findById(challenge.getId())
	             .flatMap(existingChallenge -> Mono.empty())
	             .switchIfEmpty(challengeRepository.save(challenge))
	         )
	         .subscribe();	
    }
}