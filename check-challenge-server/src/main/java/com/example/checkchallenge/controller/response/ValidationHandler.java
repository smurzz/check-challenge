package com.example.checkchallenge.controller.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;

import com.mongodb.DuplicateKeyException;

import reactor.core.publisher.Mono;

import org.springframework.context.support.DefaultMessageSourceResolvable;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ValidationHandler {

    @ExceptionHandler({WebExchangeBindException.class})
    public Mono<ResponseEntity> handleException(WebExchangeBindException e) {
        var errors = e
                .getFieldErrors()
                .stream()
                .map(fieldError -> new String( fieldError.getField() + ": " + fieldError.getDefaultMessage()))
                .collect(Collectors.toList()).toString();
        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, errors));
    }

}