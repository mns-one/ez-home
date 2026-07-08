package com.ezhome.deviceregistry.exception;

// handler to raise authentication error messages
public class AccessException extends RuntimeException {
    public AccessException(String msg) {
        super(msg);
    }
}
