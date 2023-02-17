package com.example.checkchallenge.config;

import com.example.checkchallenge.model.User;
import com.example.checkchallenge.model.UserRole;
import com.example.checkchallenge.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Set;

@Log4j2
@EnableMongoRepositories(basePackageClasses = UserRepository.class)
@EnableReactiveMongoAuditing
@Configuration
@RequiredArgsConstructor
public class MongoConfig implements CommandLineRunner {
	
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
    	Mono<Long> countMono = userRepository.count();
        countMono.subscribe(count -> {
            if (count == 0) {
                User defaultAdmin = new User(
                    "",                                			// firstName
                    "",                                         // lastname
                    "",                                         // position
                    "admin@admin.com",                          // email
                    passwordEncoder.encode("123"),  			// password
                    true,                                       // active
                    List.of(UserRole.ADMIN, UserRole.USER)      // roles
                );
                userRepository.save(defaultAdmin).subscribe(savedUser -> {
                    log.info("Default admin user created.");
                });
            }
        });
    }
}
