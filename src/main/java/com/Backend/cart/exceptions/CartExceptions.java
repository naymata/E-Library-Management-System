package com.Backend.cart.exceptions;

public class CartExceptions extends RuntimeException{
    public CartExceptions(String message) {
        super(message);
    }

    public CartExceptions(String message, Throwable cause) {
        super(message, cause);
    }

    public CartExceptions(Throwable cause) {
        super(cause);
    }
}
