package com.example.checkchallenge.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.example.checkchallenge.model.Challenge;

import reactor.core.publisher.Mono;

public interface ChallengeRepository extends ReactiveMongoRepository<Challenge, String> {
	
	Mono<Challenge> findById(String id);

}
