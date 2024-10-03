package com.zel92.user.exception;

public class RoleDoesntExistException extends RuntimeException {
    public RoleDoesntExistException(String message) {
        super(message);
    }
}
