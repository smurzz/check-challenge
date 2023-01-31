package com.example.checkchallenge.config;

import com.example.checkchallenge.model.UserRole;
import com.example.checkchallenge.repository.UserRepository;
import com.example.checkchallenge.security.jwt.JwtTokenAuthenticationFilter;
import com.example.checkchallenge.security.jwt.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http,
                                                JwtTokenProvider tokenProvider,
                                                ReactiveAuthenticationManager reactiveAuthenticationManager) {
        final String PATH_POSTS = "/post/**";

        final String PATH_AUTH = "/auth/**";
        final String PATH_USERS = "/users/*";
        final String PATH_USER = "/users/{email}";

        return http
                .cors().configurationSource(createCorsConfigSource()).and()
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .authenticationManager(reactiveAuthenticationManager)
                .securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
                .authorizeExchange(it -> it
                        .pathMatchers(HttpMethod.POST, PATH_AUTH).permitAll()
                        .pathMatchers(HttpMethod.GET, PATH_USERS).hasAuthority("ADMIN") 
                        .pathMatchers(HttpMethod.GET, PATH_USER).authenticated()
                        .pathMatchers(HttpMethod.PUT, PATH_USER).authenticated()
                        .pathMatchers(HttpMethod.DELETE, PATH_USERS).hasAuthority("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, PATH_USER).hasAuthority("USER")
                        //.pathMatchers("/me").authenticated()
                        //.pathMatchers("/users/{user}/**").access(this::currentUserMatchesPath)
                        .anyExchange().authenticated()
                )
                .addFilterAt(new JwtTokenAuthenticationFilter(tokenProvider), SecurityWebFiltersOrder.HTTP_BASIC)
                .build();


    }

    private Mono<AuthorizationDecision> currentUserMatchesPath(Mono<Authentication> authentication, AuthorizationContext context) {

        return authentication
                .map(a -> context.getVariables().get("user").equals(a.getName()))
                .map(AuthorizationDecision::new);

    }

    @Bean
    ReactiveUserDetailsService userDetailsService(UserRepository users) {

        return email -> users.findByEmail(email)
                .map(u -> User
                                .withUsername(u.getEmail())
                                .password(u.getPassword())
                                .authorities(u.getAuthorities())  //.toArray(new String[0])
                                .accountExpired(!u.isActive())
                                .credentialsExpired(!u.isActive())
                                .disabled(!u.isActive())
                                .accountLocked(!u.isActive())
                                .build()
                );
    }

    @Bean
    ReactiveAuthenticationManager reactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        var authenticationManager = new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authenticationManager.setPasswordEncoder(passwordEncoder);
        return authenticationManager;
    }

    public CorsConfigurationSource createCorsConfigSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config);
        return source;
    }

}
