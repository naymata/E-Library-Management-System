package com.Backend.user.exceptions;

public class UsernameIsTakenException extends UserException{
    public UsernameIsTakenException(String message) {
        super(message);
    }

    public UsernameIsTakenException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameIsTakenException(Throwable cause) {
        super(cause);
    }
}
