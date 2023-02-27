package com.example.checkchallenge.controller;

import static org.mockito.Mockito.when;

import java.util.List;

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
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

import com.example.checkchallenge.config.SecurityConfig;
import com.example.checkchallenge.controller.request.UserRequest;
import com.example.checkchallenge.model.User;
import com.example.checkchallenge.model.UserRole;
import com.example.checkchallenge.repository.UserRepository;
import com.example.checkchallenge.security.jwt.JwtTokenProvider;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = UserController.class)
@Import(SecurityConfig.class)
public class UserControllerTest {
	
	@Autowired
    private WebTestClient webTestClient;
	
	@MockBean
	private UserRepository userRepository;
	
	@MockBean
	private PasswordEncoder passwordEncoder;
	
	@MockBean
    private ReactiveAuthenticationManager authenticationManager;

    @MockBean
    private JwtTokenProvider tokenProvider;
	
	@InjectMocks
    private UserController userController;
	
    @Test
    @WithMockUser(authorities = "ADMIN")
    void testGetAllUsers() {
    	
    	User user1 = new User("John", "Doe", "Developer", "john.doe@example.com", passwordEncoder.encode("123"), true, List.of(UserRole.ADMIN));
    	User user2 = new User("Jane", "Doe", "Java Backend", "jane.doe@example.com", passwordEncoder.encode("123"), true, List.of(UserRole.USER));
    	
    	when(userRepository.findAll()).thenReturn(Flux.just(user1, user2));

        webTestClient.get()
        		.uri("/users")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(User.class)
                .hasSize(2)
                .contains(
                        new User("John", "Doe", "Developer", "john.doe@example.com", passwordEncoder.encode("123"), true, List.of(UserRole.ADMIN)),
                        new User("Jane", "Doe", "Java Backend", "jane.doe@example.com", passwordEncoder.encode("123"), true, List.of(UserRole.USER))
                );
    }
    
    @Test
    void testGetAllUsersUnauthorized() {
    	
    	User user1 = new User("John", "Doe", "Developer", "john.doe@example.com", passwordEncoder.encode("123"), true, List.of(UserRole.ADMIN));
    	User user2 = new User("Jane", "Doe", "Java Backend", "jane.doe@example.com", passwordEncoder.encode("123"), true, List.of(UserRole.USER));
    	
    	when(userRepository.findAll()).thenReturn(Flux.just(user1, user2));

        webTestClient.get()
        		.uri("/users")
                .exchange()
                .expectStatus().isUnauthorized()
        		.expectBody().isEmpty();
    }
    
    @Test
    @WithMockUser(authorities = {"ADMIN", "USER"})
    void testGetUserByEmailSuccess() {
    	String email = "john.doe@example.com";
    	User user1 = new User("John", "Doe", "Developer", email, passwordEncoder.encode("123"), true, List.of(UserRole.ADMIN));
    	
    	when(userRepository.findByEmail(email)).thenReturn(Mono.just(user1));
    	
    	webTestClient.get()
    		.uri("/users/{email}", email)
    		.exchange()
    		.expectStatus().isOk()
    		.expectBody(User.class)
    		.isEqualTo(user1);
    }
    
    @Test
    void testGetUserByEmailUnauthorized() {
    	String email = "john.doe@example.com";
    	User user1 = new User("John", "Doe", "Developer", email, passwordEncoder.encode("123"), true, List.of(UserRole.ADMIN));
    	
    	when(userRepository.findByEmail(email)).thenReturn(Mono.just(user1));
    	
    	webTestClient.get()
    		.uri("/users/{email}", email)
    		.exchange()
    		.expectStatus().isUnauthorized()
    		.expectBody().isEmpty();
    }
    
    @Test
    @WithMockUser(authorities = {"ADMIN", "USER"})
    void testGetUserByEmailNotFound() {
    	String email = "john.doe@example.com";
    	
    	when(userRepository.findByEmail(email)).thenReturn(Mono.empty());
    	
    	webTestClient.get()
    		.uri("/users/{email}", email)
    		.exchange()
    		.expectStatus().isNotFound()
    		.expectBody().isEmpty();
    }
    
    @Test
    @WithMockUser(authorities = {"ADMIN", "USER"})
    void testGetUserByEmailInternal() {
    	String email = "john.doe@example.com";
    	
    	when(userRepository.findByEmail(email)).thenReturn(Mono.error(new RuntimeException("Error")));
    	
    	webTestClient.get()
    		.uri("/users/{email}", email)
    		.exchange()
    		.expectStatus().is5xxServerError();
    }
    
    @Test
    @WithMockUser
    void insertUserSuccess() {
    	UserRequest userRequest1 = new UserRequest("John", "Doe", "Developer", "john.doe@example.com", "123", true, List.of(UserRole.ADMIN));
    	User user1 = new User("John", "Doe", "Developer", "john.doe@example.com", passwordEncoder.encode("123"), true, List.of(UserRole.ADMIN));

    	UserRequest userRequest2 = new UserRequest("Jane", "Doe", "Senior Software Developer", "jane.doe@example.com", "123");
    	User user2 = new User("Jane", "Doe", "Senior Software Developer", "jane.doe@example.com", passwordEncoder.encode("123"), true, List.of(UserRole.USER));

    	when(userRepository.save(user1)).thenReturn(Mono.just(user1));
    	when(userRepository.save(user2)).thenReturn(Mono.just(user2));
    	
    	webTestClient.post()
	         .uri("/users")
	         .contentType(MediaType.APPLICATION_JSON)
	         .body(Mono.just(userRequest1), UserRequest.class)
	         .exchange()
	         .expectStatus().isCreated();
    	
    	webTestClient.post()
	        .uri("/users")
	        .contentType(MediaType.APPLICATION_JSON)
	        .body(Mono.just(userRequest2), UserRequest.class)
	        .exchange()
	        .expectStatus().isCreated();
    }
    
    @Test
    void insertUserUnauthorized() {
    	UserRequest userRequest1 = new UserRequest("John", "Doe", "Developer", "john.doe@example.com", "123", true, List.of(UserRole.ADMIN));
    	User user1 = new User("John", "Doe", "Developer", "john.doe@example.com", passwordEncoder.encode("123"), true, List.of(UserRole.ADMIN));

    	when(userRepository.save(user1)).thenReturn(Mono.just(user1));

    	webTestClient.post()
	         .uri("/users")
	         .contentType(MediaType.APPLICATION_JSON)
	         .body(Mono.just(userRequest1), UserRequest.class)
	         .exchange()
	         .expectStatus().isUnauthorized();
    }
    
    @Test
    @WithMockUser
    void insertUserBadRequest() {
    	UserRequest userRequest1 = new UserRequest("John", "Doe", "Developer", "john.doe@example.com", "123", true, List.of(UserRole.ADMIN));
    	User user1 = new User("John", "Doe", "Developer", "john.doe@example.com", passwordEncoder.encode("123"), true, List.of(UserRole.ADMIN));
    	
    	UserRequest userRequest2 = new UserRequest("", "Doe", "Developer", "", "123", true, List.of(UserRole.ADMIN));
    	User user2 = new User("", "Doe", "Developer", "", passwordEncoder.encode("123"), true, List.of(UserRole.ADMIN));

    	when(userRepository.findByEmail(user1.getEmail())).thenReturn(Mono.just(user1));
    	when(userRepository.save(user1)).thenReturn(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST)));
    	
    	when(userRepository.save(user2)).thenReturn(Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST)));

    	webTestClient.post()
	         .uri("/users")
	         .contentType(MediaType.APPLICATION_JSON)
	         .body(Mono.just(userRequest1), UserRequest.class)
	         .exchange()
	         .expectStatus().isBadRequest();
    	
    	webTestClient.post()
	        .uri("/users")
	        .contentType(MediaType.APPLICATION_JSON)
	        .body(Mono.just(userRequest2), UserRequest.class)
	        .exchange()
	        .expectStatus().isBadRequest();
    }
    
    @Test
    @WithMockUser
    void insertUserInternal() {
    	UserRequest userRequest1 = new UserRequest("John", "Doe", "Developer", "john.doe@example.com", "123", true, List.of(UserRole.ADMIN));
    	User user1 = new User("John", "Doe", "Developer", "john.doe@example.com", passwordEncoder.encode("123"), true, List.of(UserRole.ADMIN));
    	
    	when(userRepository.save(user1)).thenReturn(Mono.error(new RuntimeException("Error")));

    	webTestClient.post()
	         .uri("/users")
	         .contentType(MediaType.APPLICATION_JSON)
	         .body(Mono.just(userRequest1), UserRequest.class)
	         .exchange()
	         .expectStatus().is5xxServerError();
    }
    
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateUserSuccess() {
    	String email= "john.doe@example.com";
    	UserRequest userRequestUpdated1 = new UserRequest("John", "Paul", "Developer", email, "1234", true, List.of(UserRole.ADMIN));   	
    	User user1 = new User("John", "Doe", "Developer", email, passwordEncoder.encode("123"), true, List.of(UserRole.ADMIN));
    	User userUpdated1 = new User("John", "Paul", "Developer", email, passwordEncoder.encode("1234"), true, List.of(UserRole.ADMIN));

    	when(userRepository.findByEmail(email)).thenReturn(Mono.just(user1));
    	when(userRepository.save(userUpdated1)).thenReturn(Mono.just(userUpdated1));
    	
    	webTestClient.put()
    		.uri("/users/{email}", email)
    		.contentType(MediaType.APPLICATION_JSON)
    		.body(Mono.just(userRequestUpdated1), UserRequest.class)
    		.exchange()
    		.expectStatus().isNoContent()
    		.expectBody().isEmpty();
    }
    
    @Test
    void updateUserUnauthorized() {
    	String email= "john.doe@example.com";
    	UserRequest userRequestUpdated1 = new UserRequest("John", "Paul", "Developer", email, "1234", true, List.of(UserRole.ADMIN));   	
    	User user1 = new User("John", "Doe", "Developer", email, passwordEncoder.encode("123"), true, List.of(UserRole.ADMIN));
    	User userUpdated1 = new User("John", "Paul", "Developer", email, passwordEncoder.encode("1234"), true, List.of(UserRole.ADMIN));

    	when(userRepository.findByEmail(email)).thenReturn(Mono.just(user1));
    	when(userRepository.save(userUpdated1)).thenReturn(Mono.just(userUpdated1));
    	
    	webTestClient.put()
    		.uri("/users/{email}", email)
    		.contentType(MediaType.APPLICATION_JSON)
    		.body(Mono.just(userRequestUpdated1), UserRequest.class)
    		.exchange()
    		.expectStatus().isUnauthorized();
    }
    
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateUserNotFound() {
    	String email= "john.doe@example.com";
    	UserRequest userRequestUpdated1 = new UserRequest("John", "Paul", "Developer", email, "1234", true, List.of(UserRole.ADMIN));   	
    	User userUpdated1 = new User("John", "Paul", "Developer", email, passwordEncoder.encode("1234"), true, List.of(UserRole.ADMIN));

    	when(userRepository.findByEmail(email)).thenReturn(Mono.empty());
    	when(userRepository.save(userUpdated1)).thenReturn(Mono.empty());
    	
    	webTestClient.put()
    		.uri("/users/{email}", email)
    		.contentType(MediaType.APPLICATION_JSON)
    		.body(Mono.just(userRequestUpdated1), UserRequest.class)
    		.exchange()
    		.expectStatus().isNotFound();
    }
    
    @Test
    @WithMockUser(authorities = "ADMIN")
    void updateUserInternal() {
    	String email= "john.doe@example.com";
    	User user1 = new User("John", "Doe", "Developer", email, passwordEncoder.encode("123"), true, List.of(UserRole.ADMIN));
    	UserRequest userRequestUpdated1 = new UserRequest("John", "Paul", "Developer", email, "1234", true, List.of(UserRole.ADMIN));   	
    	User userUpdated1 = new User("John", "Paul", "Developer", email, passwordEncoder.encode("1234"), true, List.of(UserRole.ADMIN));

    	when(userRepository.findByEmail(email)).thenReturn(Mono.just(user1));
    	when(userRepository.save(userUpdated1)).thenReturn(Mono.error(new RuntimeException("Error")));
    	
    	webTestClient.put()
    		.uri("/users/{email}", email)
    		.contentType(MediaType.APPLICATION_JSON)
    		.body(Mono.just(userRequestUpdated1), UserRequest.class)
    		.exchange()
    		 .expectStatus().is5xxServerError();
    }
    
    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteUserSuccess() {
    	String email= "john.doe@example.com";
    	User user = new User("John", "Doe", "Developer", email, passwordEncoder.encode("123"), true, List.of(UserRole.ADMIN));
    	
    	when(userRepository.findByEmail(email)).thenReturn(Mono.just(user));
    	when(userRepository.deleteByEmail(email)).thenReturn(Mono.just(user));
    	
    	webTestClient.delete()
    		.uri("/users/{email}", email)
    		.exchange()
    		.expectStatus().isNoContent();
    }
    
    @Test
    void deleteUserUnauthorized() {
    	String email= "john.doe@example.com";
    	User user = new User("John", "Doe", "Developer", email, passwordEncoder.encode("123"), true, List.of(UserRole.ADMIN));
    	
    	when(userRepository.findByEmail(email)).thenReturn(Mono.just(user));
    	when(userRepository.deleteByEmail(email)).thenReturn(Mono.just(user));
    	
    	webTestClient.delete()
    		.uri("/users/{email}", email)
    		.exchange()
    		.expectStatus().isUnauthorized();
    }
    
    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteUserNotFound() {
    	String email= "john.doe@example.com";
    	
    	when(userRepository.findByEmail(email)).thenReturn(Mono.empty());
    	when(userRepository.deleteByEmail(email)).thenReturn(Mono.empty());
    	
    	webTestClient.delete()
    		.uri("/users/{email}", email)
    		.exchange()
    		.expectStatus().isNotFound();
    }
    
    @Test
    @WithMockUser(authorities = "ADMIN")
    void deleteUserInternal() {
    	String email= "john.doe@example.com";
    	User user = new User("John", "Doe", "Developer", email, passwordEncoder.encode("123"), true, List.of(UserRole.ADMIN));
    	
    	when(userRepository.findByEmail(email)).thenReturn(Mono.just(user));
    	when(userRepository.deleteByEmail(email)).thenReturn(Mono.error(new RuntimeException("Error")));
    	
    	webTestClient.delete()
    		.uri("/users/{email}", email)
    		.exchange()
    		.expectStatus().is5xxServerError();
    }
    

}
