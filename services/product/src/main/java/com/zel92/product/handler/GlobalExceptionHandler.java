package com.zel92.product.handler;

import com.zel92.product.exception.AuthorizationFailedException;
import com.zel92.product.exception.ServerDownException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorResponse> handle(AuthorizationFailedException exp){
        return ResponseEntity.status(FORBIDDEN).body(new ErrorResponse(403, FORBIDDEN, exp.getMessage()));
    }

    @ExceptionHandler(ServerDownException.class)
    public ResponseEntity<ErrorResponse> handle(ServerDownException exp){
        return ResponseEntity.status(SERVICE_UNAVAILABLE).body(new ErrorResponse(500, SERVICE_UNAVAILABLE, exp.getMessage()));
    }


    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> handle(HttpClientErrorException exp){
        return ResponseEntity.status(BAD_REQUEST).body(new ErrorResponse(400, BAD_REQUEST, exp.getMessage()));
    }
}
