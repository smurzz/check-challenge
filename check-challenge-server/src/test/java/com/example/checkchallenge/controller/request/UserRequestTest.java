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

public class UserRequestTest {
	
	private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    void testValidUserRequest() {
        UserRequest userRequest = UserRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .position("Developer")
                .email("johndoe@example.com")
                .password("123")
                .build();

        assertDoesNotThrow(() -> validator.validate(userRequest));
    }
    
    @Test
    void testInvalidUserRequest() {
        UserRequest userRequest = UserRequest.builder()
                .firstName("")
                .lastName("")
                .position("Developer")
                .email("myemail")
                .password("12")
                .build();

        Set<ConstraintViolation<UserRequest>> violations = validator.validate(userRequest);
        assertEquals(4, violations.size());
        
    }

}
