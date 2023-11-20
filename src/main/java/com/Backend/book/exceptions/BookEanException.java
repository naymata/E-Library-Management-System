package com.Backend.book.exceptions;

public class BookEanException extends BookException{
    public BookEanException(String message) {
        super(message);
    }

    public BookEanException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookEanException(Throwable cause) {
        super(cause);
    }
}
