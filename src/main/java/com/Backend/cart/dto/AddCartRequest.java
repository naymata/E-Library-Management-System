package com.Backend.cart.dto;

public record AddCartRequest(
        String username,
        Long bookId,
        Short purchasedQuantity
) {
}
