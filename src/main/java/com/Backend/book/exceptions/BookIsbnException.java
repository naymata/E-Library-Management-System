package com.Backend.book.exceptions;

public class BookIsbnException extends BookException{
    public BookIsbnException(String message) {
        super(message);
    }

    public BookIsbnException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookIsbnException(Throwable cause) {
        super(cause);
    }
}
