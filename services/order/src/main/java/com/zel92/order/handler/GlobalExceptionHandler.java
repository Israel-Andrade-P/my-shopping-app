package com.zel92.order.handler;

import com.zel92.order.exception.ProductNotAvailableException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductNotAvailableException.class)
    public ResponseEntity<ErrorResponse> handle(ProductNotAvailableException exp){
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(400, BAD_REQUEST, exp.getMessage()));
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> handle(HttpClientErrorException exp){
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(400, BAD_REQUEST, exp.getMessage()));
    }
}
