package com.example.checkchallenge.controller;

import com.example.checkchallenge.controller.request.UserRequest;
import com.example.checkchallenge.model.User;
import com.example.checkchallenge.model.UserRole;
import com.example.checkchallenge.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.service.annotation.PutExchange;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @GetMapping("/users")
    public Flux<User> getAllUsers(){
        return this.userRepository.findAll();
    }
    
    @GetMapping("/users/{email}")
    public Mono<ResponseEntity<User>> getUserByEmail(@PathVariable String email){
        return this.userRepository.findByEmail(email)
        		.map(user -> ResponseEntity.ok(user))
        		.onErrorResume(error -> Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error by retrieving user")))
        		.switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @PostMapping("/users")
    public Mono<ResponseEntity> insertUser(@Valid @RequestBody UserRequest user){
    	
    	if (user.getRoles() == null || user.getRoles().isEmpty()) {
            user.setRoles(List.of(UserRole.USER));
        }
    	
        if (!user.isActive()) {
            user.setActive(true);
        }
        
        return this.userRepository
                .save(new User(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getPosition(),
                        user.getEmail(),
                        this.passwordEncoder.encode(user.getPassword()),
                        user.isActive(),
                        user.getRoles() ))
    			.map(res ->new ResponseEntity(HttpStatus.CREATED))
                .onErrorResume(org.springframework.dao.DuplicateKeyException.class, err ->
                    Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email " + user.getEmail() + " is already occupied")))
                .onErrorResume(IllegalArgumentException.class, error -> 
					Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error by creating user")));
    }
    
    @PutMapping("/users/{email}")
    public Mono<ResponseEntity> updateUser(@PathVariable String email, @Valid @RequestBody UserRequest user){
    	
    	return this.userRepository.findByEmail(email)
    			.flatMap(existingUser -> {
    				String newPass = user.getPassword();
    				String oldPass = existingUser.getPassword();
    				String encodedPass = newPass.equals(oldPass) ? oldPass : this.passwordEncoder.encode(newPass);
    				
    				existingUser.setFirstName(user.getFirstName());
    				existingUser.setLastName(user.getLastName());
    				existingUser.setPosition(user.getPosition());
    				existingUser.setPassword(encodedPass);
    				existingUser.setActive(user.isActive());
    				existingUser.setRoles(user.getRoles());
    				return userRepository.save(existingUser);
    			})
    			.map(res ->new ResponseEntity(HttpStatus.NO_CONTENT))
    			.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User with email " + email + " is not found")))
    			.onErrorResume(IllegalArgumentException.class, error -> 
    					Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error by updating user")));
    			
    }
    
    @DeleteMapping("/users/{email}")
    public Mono<ResponseEntity> deleteUser(@PathVariable String email){
    	return this.userRepository.findByEmail(email)
    			.flatMap(existingUser -> userRepository.deleteByEmail(existingUser.getEmail()))
    			.map(res -> new ResponseEntity(HttpStatus.NO_CONTENT))
    			.switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "User with email " + email + " is not found")))
    			.onErrorResume(IllegalArgumentException.class, error -> 
					Mono.error(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error by delete user")));
    }

}
