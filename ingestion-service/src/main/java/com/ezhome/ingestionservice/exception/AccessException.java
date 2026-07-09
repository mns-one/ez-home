package com.ezhome.ingestionservice.exception;

// handler to raise authentication error messages
public class AccessException extends RuntimeException {
    public AccessException(String msg) {
        super(msg);
    }
}
