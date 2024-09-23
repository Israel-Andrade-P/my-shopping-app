package com.zel92.user.exception;

public class AuthStatusException extends RuntimeException {
    public AuthStatusException(String message) {
        super(message);
    }
}
