package com.example.checkchallenge.repository;

import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.example.checkchallenge.model.Challenge;
import com.example.checkchallenge.model.Evaluation;
import com.example.checkchallenge.model.User;
import com.example.checkchallenge.model.UserRole;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
public class EvaluationRepositoryTest {
	
	@Mock
	private EvaluationRepository evaluationRepository;
	
	private User user1 = new User("Jeffrey", "Smith", "Developer", "jeffreysmith@example.com", "123", false, List.of(UserRole.ADMIN, UserRole.USER));
	private User user2 = new User("John", "Doe", "Engineer", "johndoe@example.com", "password", true, List.of(UserRole.USER));
	Challenge challenge1 = new Challenge("1", "Challenge1", "https://github.com/users/user/repos/challenge1", "description", 
			"2011-11-12T04:16:42Z", "2011-11-12T04:16:42Z", false, false, true, Map.of(), 10.0);
	Challenge challenge2 = new Challenge("2", "Challenge2", "https://github.com/users/user/repos/challenge2", "description", 
			"2011-11-12T04:16:42Z", "2011-11-12T04:16:42Z", false, true, false, Map.of(), 19.0);
	Evaluation evaluation1 = new Evaluation(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, challenge1, user1);
	Evaluation evaluation2 = new Evaluation(1, 2, 2, 1, 2, 1, 3, 1, 2, 2, 1, 2, challenge1, user2);

	
	@BeforeEach
	public void setup() {
		when(evaluationRepository.findByUser(user1)).thenReturn(Mono.just(evaluation1));
		when(evaluationRepository.findAllByChallenge(challenge1)).thenReturn(Flux.just(evaluation1, evaluation2));
	}
	
	@Test
	public void findByUserTest() {
		Mono<Evaluation> result = evaluationRepository.findByUser(user1);
		StepVerifier.create(result)
	        .expectNext(evaluation1)
	        .verifyComplete();
	}
	
	@Test
	public void findAllByChallengeTest() {
		Flux<Evaluation> result = evaluationRepository.findAllByChallenge(challenge1);
		StepVerifier.create(result)
	        .expectNext(evaluation1, evaluation2)
	        .verifyComplete();
	}
}
