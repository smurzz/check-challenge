package com.example.checkchallenge.controller;

import static org.mockito.Mockito.when;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.server.ResponseStatusException;

import com.example.checkchallenge.config.SecurityConfig;
import com.example.checkchallenge.controller.request.ChallengeRequest;
import com.example.checkchallenge.model.Challenge;
import com.example.checkchallenge.repository.ChallengeRepository;
import com.example.checkchallenge.repository.UserRepository;
import com.example.checkchallenge.security.jwt.JwtTokenProvider;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = ChallengeController.class)
@Import(SecurityConfig.class)
public class ChallengeControllerTest {
	
	@Autowired
    private WebTestClient webTestClient;
	
	@MockBean
	private ChallengeRepository challengeRepository;
	
	@MockBean
    private ReactiveAuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider tokenProvider;
    
    @MockBean
	private UserRepository userRepository;
	
	@InjectMocks
    private ChallengeController challengeController;
	
	@Test
    @WithMockUser(authorities = {"ADMIN", "USER"})
    void testGetAllChallengesSuccess() {
    	
		Challenge challenge1 = new Challenge("1", "Challenge1", "https://github.com/users/user/repos/challenge1", "description", 
				"2011-11-12T04:16:42Z", "2011-11-12T04:16:42Z", false, false, true, Map.of(), 0.0);
		Challenge challenge2 = new Challenge("2", "Challenge2", "https://github.com/users/user/repos/challenge2", "description", 
				"2011-11-12T04:16:42Z", "2011-11-12T04:16:42Z", false, true, false, Map.of("1234", 19), 19.0);
    	
    	when(challengeRepository.findAll()).thenReturn(Flux.just(challenge1, challenge2));

        webTestClient.get()
        		.uri("/challenges")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(Challenge.class)
                .hasSize(2)
                .contains(challenge1,challenge2);
    }
	
	@Test
    void testGetAllChallengesUnautherized() {
    	
		Challenge challenge1 = new Challenge("1", "Challenge1", "https://github.com/users/user/repos/challenge1", "description", 
				"2011-11-12T04:16:42Z", "2011-11-12T04:16:42Z", false, false, true, Map.of(), 0.0);
		Challenge challenge2 = new Challenge("2", "Challenge2", "https://github.com/users/user/repos/challenge2", "description", 
				"2011-11-12T04:16:42Z", "2011-11-12T04:16:42Z", false, true, false, Map.of("1234", 19), 19.0);
    	
    	when(challengeRepository.findAll()).thenReturn(Flux.just(challenge1, challenge2));

        webTestClient.get()
        		.uri("/challenges")
                .exchange()
                .expectStatus().isUnauthorized();
    }
	
	@Test
	@WithMockUser
	void getChallengeByIdTestSuccess() {
		String id = "1234";
		Challenge challenge = new Challenge("1234", "Challenge2", "https://github.com/users/user/repos/challenge2", "description", 
				"2011-11-12T04:16:42Z", "2011-11-12T04:16:42Z", false, true, false, Map.of("1234", 19), 19.0);
    	
    	when(challengeRepository.findById(id)).thenReturn(Mono.just(challenge));

        webTestClient.get()
        		.uri("/challenges/{id}", id)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Challenge.class)
                .isEqualTo(challenge);
	}
	
	@Test
	void getChallengeByIdTestUnauthorized() {
		String id = "1234";
		Challenge challenge = new Challenge("1234", "Challenge2", "https://github.com/users/user/repos/challenge2", "description", 
				"2011-11-12T04:16:42Z", "2011-11-12T04:16:42Z", false, true, false, Map.of("1234", 19), 19.0);
    	
    	when(challengeRepository.findById(id)).thenReturn(Mono.just(challenge));

        webTestClient.get()
        		.uri("/challenges/{id}", id)
                .exchange()
                .expectStatus().isUnauthorized();
	}
	
	@Test
	@WithMockUser
	void getChallengeByIdTestNotFound() {
		String id = "123";
		    	
    	when(challengeRepository.findById(id)).thenReturn(Mono.empty());

        webTestClient.get()
        		.uri("/challenges/{id}", id)
                .exchange()
                .expectStatus().isNotFound();
	}
	
	@Test
	@WithMockUser
	void getChallengeByIdTestInternal() {
		String id = "1234";
		
    	when(challengeRepository.findById(id)).thenReturn(Mono.error(new RuntimeException("Error")));

        webTestClient.get()
        		.uri("/challenges/{id}", id)
                .exchange()
                .expectStatus().is5xxServerError();
	}
	
	@Test
    @WithMockUser(authorities = "ADMIN")
    void updateChallengeSuccess() {
		String id = "1234";
		Challenge challenge = new Challenge("1234", "Challenge2", "https://github.com/users/user/repos/challenge2", "description", 
				"2011-11-12T04:16:42Z", "2011-11-12T04:16:42Z", false, true, false, Map.of("1234", 19), 19.0);
		ChallengeRequest challengeRequest = new ChallengeRequest("Challenge2", "description edited", true, false, false);
		Challenge challengeUpdated = new Challenge("1234", "Challenge2", "https://github.com/users/user/repos/challenge2", "description edited", 
				"2011-11-12T04:16:42Z", "2011-11-12T04:16:42Z", true, false, false, Map.of("1234", 19), 19.0);

    	when(challengeRepository.findById(id)).thenReturn(Mono.just(challenge));
    	when(challengeRepository.save(challengeUpdated)).thenReturn(Mono.just(challengeUpdated));
    	
    	webTestClient.put()
    		.uri("/challenges/{id}", id)
    		.contentType(MediaType.APPLICATION_JSON)
    		.body(Mono.just(challengeRequest), ChallengeRequest.class)
    		.exchange()
    		.expectStatus().isNoContent()
    		.expectBody().isEmpty();
    }
	
	@Test
    void updateChallengeAnuthorized() {
		String id = "1234";
		Challenge challenge = new Challenge("1234", "Challenge2", "https://github.com/users/user/repos/challenge2", "description", 
				"2011-11-12T04:16:42Z", "2011-11-12T04:16:42Z", false, true, false, Map.of("1234", 19), 19.0);
		ChallengeRequest challengeRequest = new ChallengeRequest("Challenge2", "description edited", true, false, false);
		Challenge challengeUpdated = new Challenge("1234", "Challenge2", "https://github.com/users/user/repos/challenge2", "description edited", 
				"2011-11-12T04:16:42Z", "2011-11-12T04:16:42Z", true, false, false, Map.of("1234", 19), 19.0);

    	when(challengeRepository.findById(id)).thenReturn(Mono.just(challenge));
    	when(challengeRepository.save(challengeUpdated)).thenReturn(Mono.just(challengeUpdated));
    	
    	webTestClient.put()
    		.uri("/challenges/{id}", id)
    		.contentType(MediaType.APPLICATION_JSON)
    		.body(Mono.just(challengeRequest), ChallengeRequest.class)
    		.exchange()
    		.expectStatus().isUnauthorized();
    }
	
	@Test
    @WithMockUser(authorities = "ADMIN")
    void updateChallengeNotFound() {
		String id = "1234";
		ChallengeRequest challengeRequest = new ChallengeRequest("Challenge2", "description edited", true, false, false);
		Challenge challengeUpdated = new Challenge("1234", "Challenge2", "https://github.com/users/user/repos/challenge2", "description edited", 
				"2011-11-12T04:16:42Z", "2011-11-12T04:16:42Z", true, false, false, Map.of("1234", 19), 19.0);

    	when(challengeRepository.findById(id)).thenReturn( Mono.empty());
    	when(challengeRepository.save(challengeUpdated)).thenReturn(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    	
    	webTestClient.put()
    		.uri("/challenges/{id}", id)
    		.contentType(MediaType.APPLICATION_JSON)
    		.body(Mono.just(challengeRequest), ChallengeRequest.class)
    		.exchange()
    		.expectStatus().isNotFound();
    }
	
	@Test
    @WithMockUser(authorities = "ADMIN")
    void updateChallengeInternal() {
		String id = "1234";
		Challenge challenge = new Challenge("1234", "Challenge2", "https://github.com/users/user/repos/challenge2", "description", 
				"2011-11-12T04:16:42Z", "2011-11-12T04:16:42Z", false, true, false, Map.of("1234", 19), 19.0);
		ChallengeRequest challengeRequest = new ChallengeRequest("Challenge2", "description edited", true, false, false);
		Challenge challengeUpdated = new Challenge("1234", "Challenge2", "https://github.com/users/user/repos/challenge2", "description edited", 
				"2011-11-12T04:16:42Z", "2011-11-12T04:16:42Z", true, false, false, Map.of("1234", 19), 19.0);

    	when(challengeRepository.findById(id)).thenReturn(Mono.just(challenge));
    	when(challengeRepository.save(challengeUpdated)).thenReturn(Mono.error(new RuntimeException("Error")));
    	
    	webTestClient.put()
    		.uri("/challenges/{id}", id)
    		.contentType(MediaType.APPLICATION_JSON)
    		.body(Mono.just(challengeRequest), ChallengeRequest.class)
    		.exchange()
    		.expectStatus().is5xxServerError();
    }

}
