package com.example.checkchallenge.controller.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.support.WebExchangeBindException;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
public class ValidationHandlerTest {
	
	@Mock
    private WebExchangeBindException exception;

    private ValidationHandler validationHandler;

    @BeforeEach
    public void setup() {
        validationHandler = new ValidationHandler();
    }
    
    @Test
    public void handleException_shouldReturnBadRequest() {
    	String field1 = "field1";
    	String field2 = "field2";
    	String objectName1 = "fieldName1";
    	String objectName2 = "fieldName2";
    	String defaultMessage1 = "message1";
    	String defaultMessage2 = "message2";
    	
        List<FieldError> fieldErrors = List.of(
            new FieldError(objectName1, field1, defaultMessage1),
            new FieldError(objectName2, field2, defaultMessage2)
        );
        when(exception.getFieldErrors()).thenReturn(fieldErrors);

        Mono<ResponseEntity> responseMono = validationHandler.handleException(exception);
        try {
            ResponseEntity responseEntity = responseMono.block();
            System.out.println(responseEntity.getBody());
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
            assertThat(responseEntity.getBody()).isEqualTo("[field1: message1, field2: message2]");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
