package com.Backend.cart.service;

import com.Backend.cart.dto.CartRequest;
import com.Backend.cart.dto.CartResponse;
import com.Backend.cart.dto.CartUserRequest;
import com.Backend.cart.dto.CartUserResponse;
import com.Backend.cart.exceptions.CartUserNotFoundException;
import com.Backend.cart.model.Cart;
import com.Backend.user.exceptions.UserNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(path = "/api/v1/cart")
public interface CartControllerService {

    @PostMapping("/add")
    CartResponse addCart(@RequestBody CartRequest request);

    @PostMapping("/get-cart")
    CartUserResponse getCart(@RequestBody CartUserRequest request) throws UserNotFoundException, CartUserNotFoundException;
}
