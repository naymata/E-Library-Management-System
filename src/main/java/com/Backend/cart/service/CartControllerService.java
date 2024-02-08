package com.Backend.cart.service;

import com.Backend.cart.dto.AddCartRequest;
import com.Backend.cart.dto.AddCartResponse;
import com.Backend.cart.dto.CartUserRequest;
import com.Backend.cart.dto.CartUserResponse;
import com.Backend.cart.exceptions.CartUserNotFoundException;
import com.Backend.user.exceptions.UserNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(path = "/api/v1/cart")
public interface CartControllerService {

    @PostMapping("/add-cart")
    AddCartResponse addCart(@RequestBody AddCartRequest request);

    @PostMapping("/get-cart")
    CartUserResponse getCart(@RequestBody CartUserRequest request) throws UserNotFoundException, CartUserNotFoundException;
}
