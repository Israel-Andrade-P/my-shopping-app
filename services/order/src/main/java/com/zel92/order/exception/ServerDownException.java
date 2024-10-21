package com.zel92.order.exception;

public class ServerDownException extends RuntimeException {
    public ServerDownException(String message) {
        super(message);
    }
}
