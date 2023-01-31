package com.example.checkchallenge.repository;

import com.example.checkchallenge.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveMongoRepository<User, String> {  //  ReactiveCrudRepository
    Flux<User> findByFirstName(String firstName);
    Flux<User> findByLastName(String lastName);
    Mono<User> findByEmail(String email);

    Mono<Boolean> existsByEmail(String email);
    Mono<User> deleteByEmail(String email);
}
