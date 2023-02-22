package com.example.checkchallenge.controller.request;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class AuthenticationRequestTest {
	
	private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    void testValidAuthenticationRequest() {
    	AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .email("johndoe@example.com")
                .password("123")
                .build();

        assertDoesNotThrow(() -> validator.validate(authenticationRequest));
    }
    
    @Test
    void testInvalidAuthenticationRequest() {
    	AuthenticationRequest authenticationRequest = new AuthenticationRequest();
    	authenticationRequest.setEmail("");
    	authenticationRequest.setPassword("");

        Set<ConstraintViolation<AuthenticationRequest>> violations = validator.validate(authenticationRequest);
        assertEquals(2, violations.size());
        
    }
    
    @Test
    void testSerialization() throws IOException, ClassNotFoundException {
        AuthenticationRequest authenticationRequest1 = AuthenticationRequest.builder()
                .email("test@example.com")
                .password("123")
                .build();
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(authenticationRequest1);
        byte[] bytes = bos.toByteArray();
        
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(bis);
        AuthenticationRequest authenticationRequest2 = (AuthenticationRequest) in.readObject();
        
        assertEquals(authenticationRequest1, authenticationRequest2);
    }

}
