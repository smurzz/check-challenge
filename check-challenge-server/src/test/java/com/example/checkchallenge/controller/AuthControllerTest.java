package com.example.checkchallenge.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.server.ResponseStatusException;

import com.example.checkchallenge.config.SecurityConfig;
import com.example.checkchallenge.controller.request.AuthenticationRequest;
import com.example.checkchallenge.controller.request.RefreshAuthenticationRequest;
import com.example.checkchallenge.controller.request.RegisterRequest;
import com.example.checkchallenge.controller.request.UserRequest;
import com.example.checkchallenge.controller.request.UserRequest.UserRequestBuilder;
import com.example.checkchallenge.repository.UserRepository;
import com.example.checkchallenge.security.jwt.JwtTokenProvider;

import reactor.core.publisher.Mono;

@WebFluxTest(controllers = AuthController.class)
@Import(SecurityConfig.class)
public class AuthControllerTest {
	
	 	@Autowired
	    private WebTestClient webTestClient;

	    @MockBean
	    private ReactiveAuthenticationManager authenticationManager;

	    @MockBean
	    private JwtTokenProvider tokenProvider;

	    @MockBean
	    private UserController userController;
	    
	    @InjectMocks
	    private AuthController authController;
	    
	    @MockBean
	    private UserRepository userRepository;
	    
	    @Test
	    void testLoginSuccess() {
	    	AuthenticationRequest authenticationRequest = new AuthenticationRequest("johndoe@example.com", "123");
	    	
	    	when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            	.thenReturn(Mono.just(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())));

		    String accessToken = "access-token";
		    when(tokenProvider.createTokenFromAuthentication(any(Authentication.class)))
		        .thenReturn(accessToken);
	    	
	    	webTestClient
	    		.post()
		    	.uri("/auth/login")
	            .contentType(MediaType.APPLICATION_JSON)
	            .body(Mono.just(authenticationRequest), AuthenticationRequest.class)
	            .exchange()
	            .expectStatus().isOk()
	            .expectBody()
	            .jsonPath("$.access_token").isEqualTo(accessToken)
	            .jsonPath("$.refresh_token").isNotEmpty();
	    }
	    
	    @Test
	    void testLoginErrors() {
	    	AuthenticationRequest aRequestEmailIsEmpty = new AuthenticationRequest("", "123");
	    	AuthenticationRequest aRequestEmailIsNull = new AuthenticationRequest(null, "123");
	    	
	    	when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            	.thenReturn(Mono.just(new UsernamePasswordAuthenticationToken(aRequestEmailIsEmpty.getEmail(), aRequestEmailIsEmpty.getPassword())));

	    	when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
    			.thenReturn(Mono.just(new UsernamePasswordAuthenticationToken(aRequestEmailIsNull.getEmail(), aRequestEmailIsNull.getPassword())));
    	
	    	webTestClient
	    		.post()
		    	.uri("/auth/login")
	            .contentType(MediaType.APPLICATION_JSON)
	            .body(Mono.just(aRequestEmailIsEmpty), AuthenticationRequest.class)
	            .exchange()
	            .expectStatus().isBadRequest();
	    	
	    	webTestClient
	    		.post()
		    	.uri("/auth/login")
	            .contentType(MediaType.APPLICATION_JSON)
	            .body(Mono.just(aRequestEmailIsNull), AuthenticationRequest.class)
	            .exchange()
	            .expectStatus().isBadRequest();
	    }
	    
	    @Test
	    void testRegisterValidUser() {
	        RegisterRequest registerRequest = new RegisterRequest("John", "Doe", "Manager", "johndoe@example.com", "123");
	       
	        UserRequest userRequest = UserRequest.builder()
	        		.firstName("John")
	        		.lastName("Doe")
	        		.position("Manager")
	        		.email("johndoe@example.com")
	        		.password("123")
	        		.build();
	       
	        ResponseEntity<Object> responseEntity = ResponseEntity.ok().build();
	        when(userController.insertUser(userRequest)).thenReturn(Mono.just(responseEntity));
	       
	        webTestClient.post()
	                .uri("/auth/register")
	                .contentType(MediaType.APPLICATION_JSON)
	                .body(Mono.just(registerRequest), RegisterRequest.class)
	                .exchange()
	                .expectStatus().isOk()
	                .expectBody()
	                .isEmpty();
	        
	    }
	    
	    @Test
	    void testRegisterInvalidUser() {
	    	RegisterRequest registerRequest = new RegisterRequest("John", "", "Manager", "", "123");
	    	 
	    	UserRequest userRequest = UserRequest.builder()
	        		.firstName("John")
	        		.lastName("Doe")
	        		.position("Manager")
	        		.email("johndoe@example.com")
	        		.password("123")
	        		.build();
	        
	    	ResponseEntity<Object> responseEntity = ResponseEntity.badRequest().build();
	    	when(userController.insertUser(userRequest)).thenReturn(Mono.just(responseEntity));
	    	
	    	webTestClient.post()
	            .uri("/auth/register")
	            .contentType(MediaType.APPLICATION_JSON)
	            .body(Mono.just(registerRequest), RegisterRequest.class)
	            .exchange()
	            .expectStatus().isBadRequest();
	    }
	    
	    @Test
	    void testRefreshSuccess() throws ExecutionException, InterruptedException {
	    	String email = "test@example.com";
	        String refreshToken = "1234567890";

	        HashMap<String, String> refreshTokenData = new HashMap<>();
	        refreshTokenData.put(email, refreshToken);
	    		     
	        RefreshAuthenticationRequest raRequest = new RefreshAuthenticationRequest(email, refreshToken);
	        
	        AuthController authController = new AuthController(refreshTokenData, tokenProvider, authenticationManager, userController);
	        WebTestClient webTestClient = WebTestClient.bindToController(authController).build();

	        String newToken = "new-token";
	        when(tokenProvider.createTokenFromEmail(email)).thenReturn(newToken);
	        
	        webTestClient.post()
	            .uri("/auth/refresh")
	            .contentType(MediaType.APPLICATION_JSON)
	            .body(Mono.just(raRequest), RefreshAuthenticationRequest.class)
	            .exchange()
	            .expectStatus().isOk()
	            .expectBody()
	            .jsonPath("$.access_token").isEqualTo(newToken);

	    }
	    
	    @Test
	    void testRefreshErrors() {
	        String email = "test@example.com";
	        String refreshToken = "1234567890";

	        HashMap<String, String> refreshTokenData = new HashMap<>();
	        refreshTokenData.put(email, refreshToken);

	        String wrongRefreshToken = "0987654321";

	        AuthController authController = new AuthController(refreshTokenData, tokenProvider, authenticationManager, userController);
	        WebTestClient webTestClient = WebTestClient.bindToController(authController).build();

	        RefreshAuthenticationRequest raRequestRTokenFalse = new RefreshAuthenticationRequest(email, wrongRefreshToken);
	        RefreshAuthenticationRequest raRequestEmailNull = new RefreshAuthenticationRequest(null, refreshToken);
	        RefreshAuthenticationRequest raRequestEmailFalse = new RefreshAuthenticationRequest("wrong@example.com", refreshToken);

	        webTestClient.post()
                .uri("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(raRequestRTokenFalse), RefreshAuthenticationRequest.class)
                .exchange()
                .expectStatus().isUnauthorized()
                .expectBody(ResponseStatusException.class);
	        
	        webTestClient.post()
	            .uri("/auth/refresh")
	            .contentType(MediaType.APPLICATION_JSON)
	            .body(Mono.just(raRequestEmailNull), RefreshAuthenticationRequest.class)
	            .exchange()
	            .expectStatus().isBadRequest()
	            .expectBody(ResponseStatusException.class);
	        
	        webTestClient.post()
	            .uri("/auth/refresh")
	            .contentType(MediaType.APPLICATION_JSON)
	            .body(Mono.just(raRequestEmailFalse), RefreshAuthenticationRequest.class)
	            .exchange()
	            .expectStatus().isUnauthorized()
	            .expectBody(ResponseStatusException.class);
	    }

}
