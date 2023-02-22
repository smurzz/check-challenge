package com.example.checkchallenge.controller.request;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

public class RegisterRequestTest {
	
	private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }
    
    @Test
    void testValidRegisterRequest() {
    	RegisterRequest registerRequest = RegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .position("Developer")
                .email("johndoe@example.com")
                .password("123")
                .build();

        assertDoesNotThrow(() -> validator.validate(registerRequest));
    }
    
    @Test
    void testInvalidRegisterRequest() {
    	RegisterRequest registerRequest = RegisterRequest.builder()
                .firstName("K")
                .lastName("Keihanaikukauakahihuliheekahaunaelekahaunaelekukauaka")
                .position("")
                .email("")
                .password("123")
                .build();

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(registerRequest);
        assertEquals(3, violations.size());
        
    }

}
