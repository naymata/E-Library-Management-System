package com.Backend.email.exceptions;

public class EmailIsInvalidException extends EmailException{
    public EmailIsInvalidException(String message) {
        super(message);
    }

    public EmailIsInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailIsInvalidException(Throwable cause) {
        super(cause);
    }
}
