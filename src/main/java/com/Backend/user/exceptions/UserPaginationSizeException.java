package com.Backend.user.exceptions;

public class UserPaginationSizeException extends UserException{
    public UserPaginationSizeException(String message) {
        super(message);
    }

    public UserPaginationSizeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserPaginationSizeException(Throwable cause) {
        super(cause);
    }
}
