package com.Backend.book.exceptions;

public class OrderByException extends BookException{
    public OrderByException(String message) {
        super(message);
    }

    public OrderByException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderByException(Throwable cause) {
        super(cause);
    }
}
