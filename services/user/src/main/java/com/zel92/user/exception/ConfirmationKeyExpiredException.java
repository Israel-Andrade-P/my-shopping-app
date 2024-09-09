package com.zel92.user.exception;

public class ConfirmationKeyExpiredException extends RuntimeException{

    public ConfirmationKeyExpiredException(String message){
        super(message);
    }
}
