package com.example.checkchallenge.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.service.annotation.PutExchange;

import com.example.checkchallenge.controller.request.UserRequest;
import com.example.checkchallenge.model.Challenge;
import com.example.checkchallenge.model.Evaluation;
import com.example.checkchallenge.model.User;
import com.example.checkchallenge.repository.ChallengeRepository;
import com.example.checkchallenge.repository.EvaluationRepository;
import com.example.checkchallenge.repository.UserRepository;
import com.example.checkchallenge.service.ChallengeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class ChallengeController {
	
	private final ChallengeRepository challengeRepository;
	private final EvaluationRepository evaluationRepository;
	
    @GetMapping("/challenges")
    public Flux<Challenge> getAllChallenges(){
        return this.challengeRepository.findAll();
    }
    
    @GetMapping("/challenges/{id}")
    public Mono<ResponseEntity<Challenge>> getChallengeById(@PathVariable String id){
        return this.challengeRepository.findById(id)
        		.map(challenge -> ResponseEntity.ok(challenge))
        		.onErrorResume(error -> Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error by retrieving challenge")))
        		.switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
    
    @PutMapping("/challenges/{id}")
    public Mono<ResponseEntity> updateChallenge(@PathVariable String id, @Valid @RequestBody Challenge challenge){ 
    	 return this.challengeRepository.findById(id)
         		.flatMap(existingChallenge -> {
         			
         			existingChallenge.setDescription(challenge.getDescription());
         			existingChallenge.setInProcessing(challenge.isInProcessing());
         			existingChallenge.setUnevaluated(challenge.isUnevaluated());
         			existingChallenge.setComleted(challenge.isComleted());
         			return challengeRepository.save(existingChallenge); 
         		})
         		.map(res ->new ResponseEntity(HttpStatus.NO_CONTENT))
    			.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Challenge with id " + id + " is not found")))
    			.onErrorResume(IllegalArgumentException.class, error -> 
    					Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error by updating challenge")));
    }
    

}
