package com.zel92.product.exception;

public class ServerDownException extends RuntimeException {
    public ServerDownException(String message) {
        super(message);
    }
}
