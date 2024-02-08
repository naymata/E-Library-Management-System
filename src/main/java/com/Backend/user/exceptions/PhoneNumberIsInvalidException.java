package com.Backend.user.exceptions;

public class PhoneNumberIsInvalidException extends PhoneNumberException{
    public PhoneNumberIsInvalidException(String message) {
        super(message);
    }

    public PhoneNumberIsInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public PhoneNumberIsInvalidException(Throwable cause) {
        super(cause);
    }
}
