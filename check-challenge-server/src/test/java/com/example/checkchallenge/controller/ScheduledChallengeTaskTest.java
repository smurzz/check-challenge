package com.example.checkchallenge.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.checkchallenge.model.Challenge;
import com.example.checkchallenge.repository.ChallengeRepository;
import com.example.checkchallenge.service.ChallengeService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
public class ScheduledChallengeTaskTest {
	
	private final String username = "noname";

	@Mock
	private ChallengeService challengeService;
	@Mock
	private ChallengeRepository challengeRepository;
	@InjectMocks
	private ScheduledChallengeTask scheduledChallengeTask;
	
	@BeforeEach
	void setUp() {
	    ReflectionTestUtils.setField(scheduledChallengeTask, "username", username);
	}
	
    @Test
    void updateChallengesRepositoriesTest() {
    	
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
		
		when(challengeService.getChallenges(username)).thenReturn(Flux.just(challenge1, challenge2));
		when(challengeRepository.existsById(challenge1.getId())).thenReturn(Mono.just(true));
	    when(challengeRepository.existsById(challenge2.getId())).thenReturn(Mono.just(false));
		when(challengeRepository.save(challenge2)).thenReturn(Mono.just(challenge2));		
		
		scheduledChallengeTask.updateChallengesRepositories();
		
		ArgumentCaptor<Challenge> challengeCaptor = ArgumentCaptor.forClass(Challenge.class);
	    verify(challengeRepository, times(1)).save(challengeCaptor.capture());
	    assertThat(challengeCaptor.getValue()).isEqualTo(challenge2);
    }

}
