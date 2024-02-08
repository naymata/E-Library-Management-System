package com.Backend.user.exceptions;

public class UserPaginationPageException extends UserException{
    public UserPaginationPageException(String message) {
        super(message);
    }

    public UserPaginationPageException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserPaginationPageException(Throwable cause) {
        super(cause);
    }
}
