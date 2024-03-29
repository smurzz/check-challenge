package com.example.checkchallenge.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.concurrent.ConcurrentHashMap;
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

import com.example.checkchallenge.config.AppConfig;
import com.example.checkchallenge.config.SecurityConfig;
import com.example.checkchallenge.controller.request.AuthenticationRequest;
import com.example.checkchallenge.controller.request.RefreshAuthenticationRequest;
import com.example.checkchallenge.controller.request.RegisterRequest;
import com.example.checkchallenge.controller.request.UserRequest;
import com.example.checkchallenge.repository.UserRepository;
import com.example.checkchallenge.security.jwt.JwtTokenProvider;

import reactor.core.publisher.Mono;

@WebFluxTest(controllers = AuthController.class)
@Import({ SecurityConfig.class, AppConfig.class })
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
		Authentication authentication = new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
				authenticationRequest.getPassword());

		ConcurrentHashMap<String, Authentication> refreshTokenData = new ConcurrentHashMap<>();

		AuthController authController = new AuthController(refreshTokenData, tokenProvider, authenticationManager,
				userController);
		WebTestClient webTestClient = WebTestClient.bindToController(authController).build();

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(Mono.just(authentication));

		String accessToken = "access-token";
		when(tokenProvider.createToken(authentication)).thenReturn(accessToken);
		when(tokenProvider.getAuthentication(accessToken)).thenReturn(authentication);

		webTestClient.post().uri("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(authenticationRequest), AuthenticationRequest.class).exchange().expectStatus().isOk()
				.expectBody().jsonPath("$.access_token").isEqualTo(accessToken).jsonPath("$.refresh_token")
				.isNotEmpty();
	}

	@Test
	void testLoginErrors() {
		AuthenticationRequest aRequestEmailIsEmpty = new AuthenticationRequest("", "123");
		AuthenticationRequest aRequestEmailIsNull = new AuthenticationRequest(null, "123");

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(Mono.just(new UsernamePasswordAuthenticationToken(aRequestEmailIsEmpty.getEmail(),
						aRequestEmailIsEmpty.getPassword())));

		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
				.thenReturn(Mono.just(new UsernamePasswordAuthenticationToken(aRequestEmailIsNull.getEmail(),
						aRequestEmailIsNull.getPassword())));

		webTestClient.post().uri("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(aRequestEmailIsEmpty), AuthenticationRequest.class).exchange().expectStatus()
				.isBadRequest();

		webTestClient.post().uri("/auth/login").contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(aRequestEmailIsNull), AuthenticationRequest.class).exchange().expectStatus()
				.isBadRequest();
	}

	@Test
	void testRegisterValidUser() {
		RegisterRequest registerRequest = new RegisterRequest("John", "Doe", "Manager", "johndoe@example.com", "123");

		UserRequest userRequest = UserRequest.builder().firstName("John").lastName("Doe").position("Manager")
				.email("johndoe@example.com").password("123").build();

		ResponseEntity<Object> responseEntity = ResponseEntity.ok().build();
		when(userController.insertUser(userRequest)).thenReturn(Mono.just(responseEntity));

		webTestClient.post().uri("/auth/register").contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(registerRequest), RegisterRequest.class).exchange().expectStatus().isOk().expectBody()
				.isEmpty();

	}

	@Test
	void testRegisterInvalidUser() {
		RegisterRequest registerRequest = new RegisterRequest("John", "", "Manager", "", "123");

		UserRequest userRequest = UserRequest.builder().firstName("John").lastName("Doe").position("Manager")
				.email("johndoe@example.com").password("123").build();

		ResponseEntity<Object> responseEntity = ResponseEntity.badRequest().build();
		when(userController.insertUser(userRequest)).thenReturn(Mono.just(responseEntity));

		webTestClient.post().uri("/auth/register").contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(registerRequest), RegisterRequest.class).exchange().expectStatus().isBadRequest();
	}

	@Test
	void testRefreshSuccess() throws ExecutionException, InterruptedException {
		String refreshToken = "0987654321";
		Authentication authentication = mock(Authentication.class);

		final ConcurrentHashMap<String, Authentication> refreshTokenData = new ConcurrentHashMap<>();
		refreshTokenData.put(refreshToken, authentication);

		RefreshAuthenticationRequest raRequest = new RefreshAuthenticationRequest("admin@admin.com", refreshToken);

		AuthController authController = new AuthController(refreshTokenData, tokenProvider, authenticationManager,
				userController);
		WebTestClient webTestClient = WebTestClient.bindToController(authController).build();

		String newToken = "new-token";
		when(authentication.getName()).thenReturn("admin@admin.com");

		when(tokenProvider.createToken(authentication)).thenReturn(newToken);

		webTestClient.post().uri("/auth/refresh").contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(raRequest), RefreshAuthenticationRequest.class).exchange().expectStatus().isOk()
				.expectBody().jsonPath("$.access_token").isEqualTo(newToken);

	}

	@Test
	void testRefreshErrors() {
		String email = "test@example.com";
		String refreshToken = "1234567890";
		Authentication authentication = mock(Authentication.class);

		ConcurrentHashMap<String, Authentication> refreshTokenData = new ConcurrentHashMap<>();
		refreshTokenData.put(refreshToken, authentication);

		String wrongRefreshToken = "0987654321";

		AuthController authController = new AuthController(refreshTokenData, tokenProvider, authenticationManager,
				userController);
		WebTestClient webTestClient = WebTestClient.bindToController(authController).build();

		when(authentication.getName()).thenReturn("test@example.com");

		RefreshAuthenticationRequest raRequestRTokenFalse = new RefreshAuthenticationRequest(email, wrongRefreshToken);
		RefreshAuthenticationRequest raRequestEmailNull = new RefreshAuthenticationRequest(null, refreshToken);
		RefreshAuthenticationRequest raRequestEmailFalse = new RefreshAuthenticationRequest("wrong@example.com",
				refreshToken);

		webTestClient.post().uri("/auth/refresh").contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(raRequestRTokenFalse), RefreshAuthenticationRequest.class).exchange().expectStatus()
				.isUnauthorized().expectBody(ResponseStatusException.class);

		webTestClient.post().uri("/auth/refresh").contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(raRequestEmailNull), RefreshAuthenticationRequest.class).exchange().expectStatus()
				.isBadRequest().expectBody(ResponseStatusException.class);

		webTestClient.post().uri("/auth/refresh").contentType(MediaType.APPLICATION_JSON)
				.body(Mono.just(raRequestEmailFalse), RefreshAuthenticationRequest.class).exchange().expectStatus()
				.isUnauthorized().expectBody(ResponseStatusException.class);
	}

}
