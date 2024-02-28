package com.Backend.email.exceptions;

public class VerificationTokenException extends RuntimeException{
    public VerificationTokenException(String message) {
        super(message);
    }

    public VerificationTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public VerificationTokenException(Throwable cause) {
        super(cause);
    }
}
