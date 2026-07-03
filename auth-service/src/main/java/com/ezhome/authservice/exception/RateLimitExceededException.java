package com.ezhome.authservice.exception;

// handler to raise rate limit error messages
public class RateLimitExceededException extends RuntimeException {
    public RateLimitExceededException(String message) {
        super(message);
    }
}