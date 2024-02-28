package com.Backend.book.exceptions;

public class BookPagesException extends BookException{
    public BookPagesException(String message) {
        super(message);
    }

    public BookPagesException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookPagesException(Throwable cause) {
        super(cause);
    }
}
