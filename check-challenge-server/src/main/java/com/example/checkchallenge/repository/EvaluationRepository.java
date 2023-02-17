package com.example.checkchallenge.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.example.checkchallenge.model.Challenge;
import com.example.checkchallenge.model.Evaluation;
import com.example.checkchallenge.model.User;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface EvaluationRepository extends ReactiveMongoRepository<Evaluation, String>{
	
	Mono<Evaluation> findByUser(User user);
	
	Flux<Evaluation> findAllByChallenge(Challenge challenge);

}
