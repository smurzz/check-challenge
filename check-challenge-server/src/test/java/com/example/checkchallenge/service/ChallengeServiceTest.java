package com.example.checkchallenge.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import com.example.checkchallenge.model.Challenge;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class ChallengeServiceTest {
	
	private final String notExistingUsername = "notexistinguser";

	private final String username = "noname";
	
	private WebClient mockClient = WebClient.builder()
            .baseUrl("https://api.github.com")
            .build();
	
	private ChallengeService challengeService = new ChallengeService(mockClient);
	
	@Test
	@WithMockUser
	void testGetNotExistngChallenges() {
		
		Flux<Challenge> result = challengeService.getChallenges(notExistingUsername);
		
		StepVerifier.create(result)
			.expectErrorMatches(throwable ->
				throwable instanceof WebClientException &&
				throwable.getMessage().contains("404 Not Found"))
	        .verify();
	}

	@Test
	@WithMockUser
	void testGetChallenges() {

		Challenge challenge1 = new Challenge();
		challenge1.setId("2760190");
		challenge1.setName("JSON-js");
		challenge1.setHtml_url("https://github.com/noname/JSON-js");
		challenge1.setDescription("JSON in JavaScript");
		challenge1.setCreated_at("2011-11-12T04:16:42Z");
		challenge1.setUpdated_at("2013-01-04T15:17:15Z");
		challenge1.setUnevaluated(true);
		challenge1.setInProcessing(false);
		challenge1.setComleted(false);

		Challenge challenge2 = new Challenge();
		challenge2.setId("3037003");
		challenge2.setName("Stop-Censorship");
		challenge2.setHtml_url("https://github.com/noname/Stop-Censorship");
		challenge2.setDescription(
				"Add to head or body of any page to automatically censor the content to protest censorship of the Internet");
		challenge2.setCreated_at("2011-12-22T22:22:38Z");
		challenge2.setUpdated_at("2013-01-04T23:04:32Z");
		challenge2.setUnevaluated(true);
		challenge2.setInProcessing(false);
		challenge2.setComleted(false);
		
		Flux<Challenge> result = challengeService.getChallenges(username);
		
		StepVerifier.create(result)
			.expectNext(challenge1)
			.expectNext(challenge2)
	        .expectComplete()
	        .verify();
	}
}
