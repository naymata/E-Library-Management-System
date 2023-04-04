package com.Backend.cart.service;

import com.Backend.cart.dto.CartRequest;
import com.Backend.cart.dto.CartResponse;
import com.Backend.cart.dto.CartUserRequest;
import com.Backend.cart.model.Cart;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CartService {

    CartResponse addCart(@RequestBody CartRequest request);
    List<Cart> getCart(@RequestBody CartUserRequest request);
}
