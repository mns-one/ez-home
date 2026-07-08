package com.ezhome.deviceservice.exception;

// handler to raise authentication error messages
public class AccessException extends RuntimeException {
    public AccessException(String msg) {
        super(msg);
    }
}
