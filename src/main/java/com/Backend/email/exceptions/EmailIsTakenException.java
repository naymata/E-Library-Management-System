package com.Backend.email.exceptions;

public class EmailIsTakenException extends EmailException{
    public EmailIsTakenException(String message) {
        super(message);
    }

    public EmailIsTakenException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailIsTakenException(Throwable cause) {
        super(cause);
    }
}
