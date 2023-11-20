package com.Backend.book.exceptions;

public class PaginationPageException extends BookException{
    public PaginationPageException(String message) {
        super(message);
    }

    public PaginationPageException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaginationPageException(Throwable cause) {
        super(cause);
    }
}
