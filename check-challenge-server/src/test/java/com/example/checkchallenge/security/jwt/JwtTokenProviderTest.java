package com.example.checkchallenge.security.jwt;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@SpringBootTest
public class JwtTokenProviderTest {

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@BeforeEach
	public void init() {
		jwtTokenProvider.init();
	}

	@Test
	void testCreateToken() {

		String email = "test@example.com";
		String password = "password123";
		List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

		Authentication authentication = new UsernamePasswordAuthenticationToken(email, password, authorities);

		String token = jwtTokenProvider.createToken(authentication);

		assertNotNull(token);
		assertTrue(jwtTokenProvider.validateToken(token));

	}

}
