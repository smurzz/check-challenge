package com.example.checkchallenge.repository;

import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.example.checkchallenge.model.Challenge;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
public class ChallengeRepositoryTest {
	
	@Mock
	private ChallengeRepository challengeRepository;
	
	private Challenge challenge1 = new Challenge("1", "Challenge1", "https://github.com/users/user/repos/challenge1", "description", 
			"2011-11-12T04:16:42Z", "2011-11-12T04:16:42Z", false, false, true, Map.of(), 10.0);
	private Challenge challenge2 = new Challenge("2", "Challenge2", "https://github.com/users/user/repos/challenge2", "description", 
			"2011-11-12T04:16:42Z", "2011-11-12T04:16:42Z", false, true, false, Map.of(), 19.0);
	
	@BeforeEach
	public void setup() {
		when(challengeRepository.findById(challenge1.getId())).thenReturn(Mono.just(challenge1));
		when(challengeRepository.findById(challenge2.getId())).thenReturn(Mono.empty());
	}
	
	@Test
	public void findByIdTest() {
		Mono<Challenge> result1 = challengeRepository.findById("1");
		StepVerifier.create(result1)
	        .expectNext(challenge1)
	        .verifyComplete();
		
		Mono<Challenge> result2 = challengeRepository.findById("2");
		StepVerifier.create(result2)
			.expectComplete()
	        .verify();
	}

}
