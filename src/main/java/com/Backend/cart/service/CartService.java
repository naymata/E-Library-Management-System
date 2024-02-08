package com.Backend.cart.service;

import com.Backend.cart.dto.AddCartRequest;
import com.Backend.cart.dto.AddCartResponse;
import com.Backend.cart.dto.CartUserRequest;
import com.Backend.cart.dto.CartUserResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface CartService {

    AddCartResponse addCart(@RequestBody AddCartRequest request);
    CartUserResponse getCart(@RequestBody CartUserRequest request);
}
