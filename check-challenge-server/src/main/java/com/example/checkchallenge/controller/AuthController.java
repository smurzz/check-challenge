package com.example.checkchallenge.controller;

import com.example.checkchallenge.controller.request.AuthenticationRequest;
import com.example.checkchallenge.controller.request.RefreshAuthenticationRequest;
import com.example.checkchallenge.controller.request.RegisterRequest;
import com.example.checkchallenge.controller.request.UserRequest;
import com.example.checkchallenge.model.User;
import com.example.checkchallenge.model.UserRole;
import com.example.checkchallenge.repository.UserRepository;
import com.example.checkchallenge.security.jwt.JwtTokenProvider;
import com.fasterxml.uuid.Generators;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import jakarta.validation.Valid;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static java.util.stream.Collectors.joining;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private HashMap<String, String> refreshTokenData = new HashMap<>();
    private final JwtTokenProvider tokenProvider;
    private final ReactiveAuthenticationManager authenticationManager;
    private final UserController userController;

    @PostMapping("/login")
    public Mono<ResponseEntity> login(@Valid @RequestBody AuthenticationRequest authRequest) {

        return this.authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()))
                    .onErrorResume(AccountStatusException.class, err ->
                            Mono.error(new ResponseStatusException(HttpStatus.FORBIDDEN, err.getMessage())))
                    .onErrorResume(BadCredentialsException.class, err ->
                            Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, err.getMessage())))
                .map(this.tokenProvider::createTokenFromAuthentication)
                .map(accessToken -> {
                    HttpHeaders httpHeaders = new HttpHeaders();
                    httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

                    UUID refreshToken = Generators.randomBasedGenerator().generate();
                    refreshTokenData.put(authRequest.getEmail(), refreshToken.toString());
                    Map<Object, Object> data = new HashMap<>();
                    data.put("access_token", accessToken);
                    data.put("refresh_token", refreshToken);

                    return new ResponseEntity<>(data, httpHeaders, HttpStatus.OK);
                });
    }

    @PostMapping("/register")
    public Mono<ResponseEntity> registerUser(@Valid @RequestBody RegisterRequest registerRequest){
        UserRequest userRequest1 = new UserRequest(
                registerRequest.getFirstName(),
                registerRequest.getLastName(),
                registerRequest.getPosition(),
                registerRequest.getEmail(),
                registerRequest.getPassword()
        );
       return userController.insertUser(userRequest1);
    }

    @PostMapping("/refresh")
    public Mono<ResponseEntity> refresh(@Valid @RequestBody RefreshAuthenticationRequest raRequest) throws ExecutionException, InterruptedException {
        String email = raRequest.getEmail();
        String refreshToken = raRequest.getRefreshToken();

        if (refreshTokenData.get(email) == null) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        }

        if (refreshTokenData.get(email).equals(refreshToken)) {
            String newToken = this.tokenProvider.createTokenFromEmail(email);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + newToken);
            return Mono.just(new ResponseEntity<>(Map.of("access_token", newToken), httpHeaders, HttpStatus.OK));
        }
        return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED));
   }
}