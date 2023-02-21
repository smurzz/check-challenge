package com.example.checkchallenge.repository;

import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.example.checkchallenge.model.Challenge;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
public class ChallengeRepositoryTest {
	
	@Autowired
	private ChallengeRepository challengeRepository;
	
	private Challenge challenge1 = new Challenge("1", "Challenge1", "https://github.com/users/user/repos/challenge1", "description", 
			"2011-11-12T04:16:42Z", "2011-11-12T04:16:42Z", false, false, true, Map.of(), 10.0);
	private Challenge challenge2 = new Challenge("2", "Challenge2", "https://github.com/users/user/repos/challenge2", "description", 
			"2011-11-12T04:16:42Z", "2011-11-12T04:16:42Z", false, true, false, Map.of(), 19.0);
	
	@AfterEach
	public void clean() {
		challengeRepository.deleteAll(Flux.just(challenge1, challenge2)).block();
	}
	
	@BeforeEach
	public void create() {
		challengeRepository.saveAll(Flux.just(challenge1, challenge2)).blockLast();
	}
	
	@Test
	public void findByIdTest() {
		Mono<Challenge> result1 = challengeRepository.findById("1");
		StepVerifier.create(result1)
	        .expectNext(challenge1)
	        .verifyComplete();
		
		Mono<Challenge> result2 = challengeRepository.findById("2");
		StepVerifier.create(result2)
	        .expectNext(challenge2)
	        .verifyComplete();
	}

}
