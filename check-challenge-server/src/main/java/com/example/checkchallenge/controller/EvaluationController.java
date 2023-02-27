package com.example.checkchallenge.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.example.checkchallenge.controller.request.EvaluationRequest;
import com.example.checkchallenge.model.Challenge;
import com.example.checkchallenge.model.Evaluation;
import com.example.checkchallenge.model.User;
import com.example.checkchallenge.repository.ChallengeRepository;
import com.example.checkchallenge.repository.EvaluationRepository;
import com.example.checkchallenge.repository.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class EvaluationController {

	private final EvaluationRepository evaluationRepository;
	private final UserRepository userRepository;
	private final ChallengeRepository challengeRepository;

	@PostMapping("/evaluations")
	public Mono<ResponseEntity<?>> insertEvaluation(@Valid @RequestBody EvaluationRequest evaluationRequest) {

		return Mono
				.zip(userRepository.findByEmail(evaluationRequest.getUserEmail())
						.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
								"User with email " + evaluationRequest.getUserEmail() + " is not found"))),
						challengeRepository.findById(evaluationRequest.getChallengeId())
								.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND,
										"Challenge with id " + evaluationRequest.getChallengeId() + " is not found"))))
				.flatMap(tuple -> {
					User user = tuple.getT1();
					Challenge challenge = tuple.getT2();
					Evaluation evaluation = new Evaluation(
							Integer.parseInt( evaluationRequest.getImplementationCriteria() ),
							Integer.parseInt( evaluationRequest.getAlgorithmicCriteria() ),
							Integer.parseInt( evaluationRequest.getStructureCriteria() ),
							Integer.parseInt( evaluationRequest.getErrorHandlingCriteria() ),
							Integer.parseInt( evaluationRequest.getFormattingCriteria() ),
							Integer.parseInt( evaluationRequest.getCommitHistoryCriteria() ),
							Integer.parseInt( evaluationRequest.getReadmeCriteria() ),
							Integer.parseInt( evaluationRequest.getTestQualityCriteria() ),
							Integer.parseInt( evaluationRequest.getDesignPatternsCriteria() ),
							Integer.parseInt( evaluationRequest.getStylingCriteria() ),
							Integer.parseInt( evaluationRequest.getCiCdCriteria() ),
							Integer.parseInt( evaluationRequest.getDockerCriteria() ), 
							challenge, 
							user);
					challenge.setUnevaluated(false);
					
					return evaluationRepository.findByUser(user)
							 .flatMap(existingEvaluation ->
						        evaluationRepository.delete(existingEvaluation)
						            .then(evaluationRepository.save(evaluation)))
						    .switchIfEmpty(evaluationRepository.save(evaluation))
						    .doOnSuccess(savedEvaluation -> {
						    	Map<String, Integer> listEvaluations = challenge.getEvaluationsInfo();
                                listEvaluations.put(savedEvaluation.getId(), savedEvaluation.getScore());
						    	challenge.setUnevaluated(false);
                            	challenge.setInProcessing(true);
                            	challenge.setAverageScore(challenge.countAverageScore(savedEvaluation.getMaxScore()));
                            	challengeRepository.save(challenge)
                            		.subscribe();
						    })
							.map(res -> new ResponseEntity<>(HttpStatus.CREATED))
							.onErrorResume(error -> Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
											"Error by creating evaluation")));
				});
	}

}
