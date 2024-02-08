package com.Backend.user.exceptions;

public class PhoneNumberException extends RuntimeException{
    public PhoneNumberException(String message) {
        super(message);
    }

    public PhoneNumberException(String message, Throwable cause) {
        super(message, cause);
    }

    public PhoneNumberException(Throwable cause) {
        super(cause);
    }
}
