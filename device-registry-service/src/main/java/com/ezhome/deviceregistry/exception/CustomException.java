package com.ezhome.deviceregistry.exception;

// handler to raise custom error messages
public class CustomException extends RuntimeException {
    public CustomException(String msg){
        super(msg);
    }
}
