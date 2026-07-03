package com.ezhome.authservice.exception;

// handler to raise custom error messages
public class CustomException extends RuntimeException {
    public CustomException(String msg){
        super(msg);
    }
}
