package com.example.checkchallenge.controller.response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.MapBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
	void handleException_shouldReturnBadRequest() throws NoSuchMethodException {
		String field1 = "field1";
		String field2 = "field2";
		String objectName1 = "fieldName1";
		String objectName2 = "fieldName2";
		String defaultMessage1 = "message1";
		String defaultMessage2 = "message2";
		List<FieldError> fieldErrors = List.of(new FieldError(objectName1, field1, defaultMessage1),
				new FieldError(objectName2, field2, defaultMessage2));

		Method method = new Object() {
		}.getClass().getEnclosingMethod();
		MethodParameter parameter = mock(MethodParameter.class);
		lenient().when(parameter.getMethod()).thenReturn(method);

		BindingResult bindingResult = mock(BindingResult.class);
		when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);

		WebExchangeBindException exception = new WebExchangeBindException(parameter, bindingResult);

		ResponseEntity<List<String>> responseEntity = validationHandler.handleException(exception).block();

		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(responseEntity.getBody().size()).isEqualTo(2);
		assertThat(responseEntity.getBody().get(0)).isEqualTo("field1: message1");
		assertThat(responseEntity.getBody().get(1)).isEqualTo("field2: message2");
	}

}
