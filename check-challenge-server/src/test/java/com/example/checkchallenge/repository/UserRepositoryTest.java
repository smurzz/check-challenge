package com.example.checkchallenge.repository;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.example.checkchallenge.model.User;
import com.example.checkchallenge.model.UserRole;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@DataMongoTest
public class UserRepositoryTest {
	
	@Autowired
    private UserRepository userRepository;
	
	private User user1 = new User("Jeffrey", "Smith", "Developer", "jeffreysmith@example.com", "123", false, List.of(UserRole.ADMIN, UserRole.USER));
	private User user2 = new User("John", "Doe", "Engineer", "johndoe@example.com", "password", true, List.of(UserRole.USER));
	private User user3 = new User("Jeffrey", "Joe", "software developer", "jeffreyjoe@example.com", "1234", true, List.of(UserRole.ADMIN));
	private User user4 = new User("Kate", "Doe", "software developer", "katedoe@example.com", "1234", true, List.of(UserRole.ADMIN, UserRole.USER));

	
	@AfterEach
	public void clean() {
		userRepository.deleteAll(Flux.just(user1, user2, user3, user4)).block();
	}
	
	@BeforeEach
	public void create() {
		userRepository.saveAll(Flux.just(user1, user2, user3, user4)).blockLast();
	}
	
	@Test
	public void findByFirstNameTest() {		
		Flux<User> result = userRepository.findByFirstName("Jeffrey");
		StepVerifier.create(result)
	        .expectNext(user1, user3)
	        .verifyComplete();	
	}
	
	@Test
	public void findByLastNameTest() {
		Flux<User> result = userRepository.findByLastName("Doe");
		StepVerifier.create(result)
	        .expectNext(user2, user4)
	        .verifyComplete();
	}
	
	@Test
	public void findByEmailTest() {
		Mono<User> result = userRepository.findByEmail("johndoe@example.com");
		StepVerifier.create(result)
	        .expectNext(user2)
	        .verifyComplete();
	}

	@Test
	public void findByEmailNotExistsTest() {
		Mono<User> result = userRepository.findByEmail("notexists@example.com");
		StepVerifier.create(result)
			.verifyComplete();
	}

	@Test
	public void existsByEmailTest() {
		Mono<Boolean> result1 = userRepository.existsByEmail("johndoe@example.com");
		StepVerifier.create(result1)
	        .expectNext(true)
	        .verifyComplete();
		
		var result2 = userRepository.existsByEmail("notexists@example.com");
		StepVerifier.create(result2)
	        .expectNext(false)
	        .verifyComplete();
	}
	
	@Test
	public void deleteByEmailTest() {
		User user5 = new User("Lila", "Smith", "Developer", "lilasmith@example.com", "123", false, List.of(UserRole.USER));		
		Mono<User> result = userRepository
				.save(user5)
				.then(userRepository
						.deleteByEmail(user5.getEmail()));
		
		StepVerifier.create(result)
	        .expectNext(user5)
	        .verifyComplete();
	}


}
