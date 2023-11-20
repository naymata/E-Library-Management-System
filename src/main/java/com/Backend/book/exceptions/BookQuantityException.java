package com.Backend.book.exceptions;

public class BookQuantityException extends BookException{
    public BookQuantityException(String message) {
        super(message);
    }

    public BookQuantityException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookQuantityException(Throwable cause) {
        super(cause);
    }
}
