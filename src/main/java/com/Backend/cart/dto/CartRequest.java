package com.Backend.cart.dto;

public record CartRequest(
        String username,
        Long bookId
) {
}
