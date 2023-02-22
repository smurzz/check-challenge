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

public class RefreshAuthenticationRequestTest {
	
	private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @Test
    void testValidRefreshAuthenticationRequest() {
    	RefreshAuthenticationRequest refreshAuthenticationRequest = RefreshAuthenticationRequest.builder()
                .email("johndoe@example.com")
                .refreshToken("f43eb6ef-f0b7-4253-beb6-eff0b782539d")
                .build();

        assertDoesNotThrow(() -> validator.validate(refreshAuthenticationRequest));
    }
    
    @Test
    void testInvalidRefreshAuthenticationRequest() {
    	RefreshAuthenticationRequest refreshAuthenticationRequest = RefreshAuthenticationRequest.builder()
    			.email("myemail")
                .refreshToken("")
                .build();

        Set<ConstraintViolation<RefreshAuthenticationRequest>> violations = validator.validate(refreshAuthenticationRequest);
        assertEquals(2, violations.size());
        
    }
    
    @Test
    void testSerialization() throws IOException, ClassNotFoundException {
        RefreshAuthenticationRequest refreshAuthenticationRequest1 = RefreshAuthenticationRequest.builder()
                .email("test@example.com")
                .refreshToken("f43eb6ef-f0b7-4253-beb6-eff0b782539d")
                .build();
        
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(refreshAuthenticationRequest1);
        byte[] bytes = bos.toByteArray();
        
        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(bis);
        RefreshAuthenticationRequest refreshAuthenticationRequest2 = (RefreshAuthenticationRequest) in.readObject();
        
        assertEquals(refreshAuthenticationRequest1, refreshAuthenticationRequest2);
    }

}
