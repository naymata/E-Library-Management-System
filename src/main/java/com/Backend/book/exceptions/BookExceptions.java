package com.Backend.book.exceptions;

public class BookExceptions extends RuntimeException{
    public BookExceptions(String message) {
        super(message);
    }

    public BookExceptions(String message, Throwable cause) {
        super(message, cause);
    }

    public BookExceptions(Throwable cause) {
        super(cause);
    }
}
