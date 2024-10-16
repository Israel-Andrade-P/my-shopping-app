package com.zel92.user.handler;

import com.zel92.user.exception.ConfirmationKeyExpiredException;
import com.zel92.user.exception.CustomInvalidKeyException;
import com.zel92.user.exception.RoleDoesntExistException;
import com.zel92.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle(UserNotFoundException exception){
        return ResponseEntity.badRequest().body(
                new ErrorResponse(401, HttpStatus.BAD_REQUEST, exception.getMessage())
        );
    }

    @ExceptionHandler(CustomInvalidKeyException.class)
    public ResponseEntity<ErrorResponse> handle(CustomInvalidKeyException exception){
        return ResponseEntity.badRequest().body(
                new ErrorResponse(401, HttpStatus.BAD_REQUEST, exception.getMessage())
        );
    }

    @ExceptionHandler(ConfirmationKeyExpiredException.class)
    public ResponseEntity<ErrorResponse> handle(ConfirmationKeyExpiredException exception){
        return ResponseEntity.badRequest().body(
                new ErrorResponse(404, HttpStatus.NOT_FOUND, exception.getMessage())
        );
    }

    @ExceptionHandler(RoleDoesntExistException.class)
    public ResponseEntity<ErrorResponse> handle(RoleDoesntExistException exception){
        return ResponseEntity.badRequest().body(
                new ErrorResponse(404, HttpStatus.NOT_FOUND, exception.getMessage())
        );
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException exception){
        HashMap<String, String> errors = new HashMap<>();

        exception.getBindingResult().getAllErrors().forEach(error -> {
            var fieldName = ((FieldError)error).getField();
            var message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(404, HttpStatus.BAD_REQUEST, "Invalid fields", errors));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception exception){
        return ResponseEntity.badRequest().body(
                new ErrorResponse(500, HttpStatus.INTERNAL_SERVER_ERROR, "Oops! Something went wrong. An unexpected error has occurred: " + exception.getMessage())
        );
    }

}
