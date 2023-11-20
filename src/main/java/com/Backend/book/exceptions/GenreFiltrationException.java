package com.Backend.book.exceptions;

public class GenreFiltrationException extends BookException {
    public GenreFiltrationException(String message) {
        super(message);
    }

    public GenreFiltrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenreFiltrationException(Throwable cause) {
        super(cause);
    }
}
