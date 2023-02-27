package com.example.checkchallenge.repository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.server.ResponseStatusException;

import com.example.checkchallenge.config.SecurityConfig;
import com.example.checkchallenge.controller.EvaluationController;
import com.example.checkchallenge.controller.UserController;
import com.example.checkchallenge.controller.request.EvaluationRequest;
import com.example.checkchallenge.model.Challenge;
import com.example.checkchallenge.model.Evaluation;
import com.example.checkchallenge.model.User;
import com.example.checkchallenge.model.UserRole;
import com.example.checkchallenge.security.jwt.JwtTokenProvider;

import reactor.core.publisher.Mono;

@WebFluxTest(controllers = EvaluationController.class)
@Import(SecurityConfig.class)
public class EvaluationControllerTest {
	
	@Autowired
    private WebTestClient webTestClient;
	
	@MockBean
	private UserRepository userRepository;
	
	@MockBean
	private ChallengeRepository challengeRepository;
	
	@MockBean
	private EvaluationRepository evaluationRepository;
	
	@MockBean
    private ReactiveAuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider tokenProvider;
    
    @MockBean
	private PasswordEncoder passwordEncoder;
	
	@InjectMocks
    private EvaluationController evaluationController;
	
	
	@Test
	@WithMockUser
	void insertNotExistingEvaluationTestSuccess() {
	    String userEmail = "john.doe@example.com";
	    String challengeId = "1234";
	    
	    EvaluationRequest evaluationRequest = new EvaluationRequest("2", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", challengeId, userEmail);
	    User user = new User("John", "Doe", "Developer", userEmail, passwordEncoder.encode("123"), true, List.of(UserRole.ADMIN));
	    Challenge challengeBeforeEvaluation = new Challenge(challengeId, "Challenge1", "https://github.com/users/user/repos/challenge1", "description", 
	    		"2023-02-19T15:30:00Z", "2023-02-21T15:30:00Z", false, false, true, new HashMap<>(), 0.0);
	    Evaluation evaluation = new Evaluation(2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, challengeBeforeEvaluation, user);
	    evaluation.setId("123");
	    Challenge challengeAfterEvaluation = new Challenge(challengeId, "Challenge1", "https://github.com/users/user/repos/challenge1", "description", 
	    		"2023-02-19T15:30:00Z", "2023-02-21T15:30:00Z", false, true, false, Map.of("1234", 13), 13.0);

	    when(userRepository.findByEmail(userEmail)).thenReturn(Mono.just(user));
	    when(challengeRepository.findById(challengeId)).thenReturn(Mono.just(challengeBeforeEvaluation));
	    when(evaluationRepository.findByUser(user)).thenReturn(Mono.empty());
	    when(evaluationRepository.save(any(Evaluation.class))).thenReturn(Mono.just(evaluation));
	    when(challengeRepository.save(any(Challenge.class))).thenReturn(Mono.just(challengeAfterEvaluation));

	    webTestClient.post()
	        .uri("/evaluations")
	        .contentType(MediaType.APPLICATION_JSON)
	        .bodyValue(evaluationRequest)
	        .exchange()
	        .expectStatus().isCreated();
	}
	
	@Test
	@WithMockUser
	void insertExistingEvaluationTestSuccess() {
	    String userEmail = "john.doe@example.com";
	    String challengeId = "1234";
	    
	    EvaluationRequest evaluationRequest = new EvaluationRequest("2", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", challengeId, userEmail);
	    User user = new User("John", "Doe", "Developer", userEmail, passwordEncoder.encode("123"), true, List.of(UserRole.ADMIN));
	    Challenge challengeBeforeEvaluation = new Challenge(challengeId, "Challenge1", "https://github.com/users/user/repos/challenge1", "description", 
	    		"2023-02-19T15:30:00Z", "2023-02-21T15:30:00Z", false, false, true, new HashMap<>(), 0.0);
	    Evaluation evaluation = new Evaluation(2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, challengeBeforeEvaluation, user);
	    evaluation.setId("123");
	    Challenge challengeAfterEvaluation = new Challenge(challengeId, "Challenge1", "https://github.com/users/user/repos/challenge1", "description", 
	    		"2023-02-19T15:30:00Z", "2023-02-21T15:30:00Z", false, true, false, Map.of("1234", 13), 13.0);

	    when(userRepository.findByEmail(userEmail)).thenReturn(Mono.just(user));
	    when(challengeRepository.findById(challengeId)).thenReturn(Mono.just(challengeBeforeEvaluation));
	    when(evaluationRepository.findByUser(user)).thenReturn(Mono.just(evaluation));
	    when(evaluationRepository.delete(evaluation)).thenReturn(Mono.empty());
	    when(evaluationRepository.save(any(Evaluation.class))).thenReturn(Mono.just(evaluation));
	    when(challengeRepository.save(any(Challenge.class))).thenReturn(Mono.just(challengeAfterEvaluation));

	    webTestClient.post()
	        .uri("/evaluations")
	        .contentType(MediaType.APPLICATION_JSON)
	        .bodyValue(evaluationRequest)
	        .exchange()
	        .expectStatus().isCreated();
	}
	
	@Test
	void insertExistingEvaluationTestUnauthorized() {
	    String userEmail = "john.doe@example.com";
	    String challengeId = "1234";
	    
	    EvaluationRequest evaluationRequest = new EvaluationRequest("2", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", challengeId, userEmail);

	    webTestClient.post()
	        .uri("/evaluations")
	        .contentType(MediaType.APPLICATION_JSON)
	        .bodyValue(evaluationRequest)
	        .exchange()
	        .expectStatus().isUnauthorized();
	}
	
	@Test
	@WithMockUser
	void insertEvaluationTestUserAndChallengNotFound() {
	    String userEmail = "john.doe@example.com";
	    String challengeId = "1234";
	    
	    EvaluationRequest evaluationRequest = new EvaluationRequest("2", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", challengeId, userEmail);

	    when(userRepository.findByEmail(userEmail)).thenReturn(Mono.empty());
	    when(challengeRepository.findById(challengeId)).thenReturn(Mono.empty());

	    webTestClient.post()
	        .uri("/evaluations")
	        .contentType(MediaType.APPLICATION_JSON)
	        .bodyValue(evaluationRequest)
	        .exchange()
	        .expectStatus().isNotFound();
	}
	
	@Test
	@WithMockUser
	void insertEvaluationTestInternal() {
	    String userEmail = "john.doe@example.com";
	    String challengeId = "1234";

	    EvaluationRequest evaluationRequest = new EvaluationRequest("2", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", challengeId, userEmail);

	    when(userRepository.findByEmail(userEmail)).thenReturn(Mono.just(new User()));
	    when(challengeRepository.findById(challengeId)).thenReturn(Mono.just(new Challenge()));
	    when(evaluationRepository.findByUser(any(User.class))).thenReturn(Mono.empty());
	    when(evaluationRepository.save(any(Evaluation.class))).thenReturn(Mono.error(new RuntimeException("Error saving evaluation")));

	    webTestClient.post()
	        .uri("/evaluations")
	        .contentType(MediaType.APPLICATION_JSON)
	        .bodyValue(evaluationRequest)
	        .exchange()
	        .expectStatus().is5xxServerError();
	}

}
