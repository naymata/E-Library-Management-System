package com.Backend.book.exceptions;

public class PaginationSizeException extends BookException{
    public PaginationSizeException(String message) {
        super(message);
    }

    public PaginationSizeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaginationSizeException(Throwable cause) {
        super(cause);
    }
}
