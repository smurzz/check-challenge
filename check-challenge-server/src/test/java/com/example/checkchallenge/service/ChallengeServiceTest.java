package com.example.checkchallenge.service;

import static org.hamcrest.CoreMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.checkchallenge.model.Challenge;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ChallengeServiceTest {
	
	@Value("${github.username}")
	private String username;
	
	 @Mock
	 private WebClient webClient;
	 
	 @InjectMocks
	 private ChallengeService challengeService;
	 
	 @Test
	 void testGetChallenges() {
		 
		 Challenge challenge1 = new Challenge();
		 challenge1.setId("lkhkj434b2hj65745jhbk5b54");
		 challenge1.setName("testChallenge1");
		 challenge1.setHtml_url("https://gihub.com/accountName/testChallenge1");
		 challenge1.setDescription("Test challenge1");
		 challenge1.setCreated_at("01.01.2021");
		 challenge1.setUpdated_at("01.01.2021");
		 challenge1.setUnevaluated(true);
		 challenge1.setInProcessing(false);
		 challenge1.setComleted(false);
		 
		 Challenge challenge2 = new Challenge();
		 challenge2.setId("nkjnk3452lkjb25b3jh4jjnkl234");
		 challenge2.setName("testChallenge2");
		 challenge2.setHtml_url("https://gihub.com/accountName/testChallenge2");
		 challenge2.setDescription("Test challenge2");
		 challenge2.setCreated_at("01.01.2022");
		 challenge2.setUpdated_at("01.01.2022");
		 challenge2.setUnevaluated(false);
		 challenge2.setInProcessing(true);
		 challenge2.setComleted(false);
		 challenge2.setEvaluationsInfo(new HashMap<String, Integer>(Map.of("kjdfgakjhg23534", 24)));
		 challenge2.setAverageScore(24);
		 
		 // 1.
		 when(webClient
				 .get())
		 .thenReturn(mock(WebClient.RequestHeadersUriSpec.class));
		// 2.
		 when(webClient
				 .get()
				 .uri("https://github.com/users/{login}/repos", username)
				 .retrieve()
				 .bodyToFlux(Challenge.class));
		// 3.
		 when(webClient
				 .get()
				 .uri("https://github.com/users/{login}/repos", username)
				 .retrieve()
				 .bodyToFlux(Challenge.class))
         .thenReturn(Flux.just(challenge1, challenge2));
		 
		 Flux<Challenge> challenges = challengeService.getChallenges();
		 
		 StepVerifier
		 	.create(challenges)
		 	.expectNext(challenge1)
		 	.expectNext(challenge2)
         	.verifyComplete();
	 }

}
