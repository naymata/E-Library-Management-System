package com.Backend.cart.exceptions;

public class CartUserNotFoundException extends CartException{
    public CartUserNotFoundException(String message) {
        super(message);
    }

    public CartUserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CartUserNotFoundException(Throwable cause) {
        super(cause);
    }
}
