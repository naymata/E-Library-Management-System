package com.Backend.book.exceptions;

public class BookFieldException extends BookException{
    public BookFieldException(String message) {
        super(message);
    }

    public BookFieldException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookFieldException(Throwable cause) {
        super(cause);
    }
}
