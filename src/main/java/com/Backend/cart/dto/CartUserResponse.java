package com.Backend.cart.dto;

import com.Backend.cart.model.Cart;

import java.util.List;

public record CartUserResponse(
        Integer status,
        List<Cart> data
) {
}
