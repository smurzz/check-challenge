package com.example.checkchallenge.security.jwt;

import com.example.checkchallenge.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest
@AutoConfigureWebTestClient
class RoleAuthenticationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @WithMockUser(username = "admin@admin", roles = {"ADMIN"})
    void testUserAccess() {
        webTestClient.get().uri("/users").exchange().expectStatus().isOk();
    }
}
