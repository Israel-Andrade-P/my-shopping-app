package com.zel92.gateway.exception;

public class ServerDownException extends RuntimeException {
    public ServerDownException(String message) {
        super(message);
    }
}
