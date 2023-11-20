package com.Backend.book.exceptions;

public class BookListEmptyException extends BookException{
    public BookListEmptyException(String message) {
        super(message);
    }

    public BookListEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookListEmptyException(Throwable cause) {
        super(cause);
    }
}
