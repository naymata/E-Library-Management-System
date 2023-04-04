package com.Backend.cart.service;

import com.Backend.cart.dto.CartRequest;
import com.Backend.cart.dto.CartResponse;
import com.Backend.cart.dto.CartUserRequest;
import com.Backend.cart.model.Cart;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(path = "/api/v1/cart")
public interface CartControllerService {

    @PostMapping("/add")
    ResponseEntity<CartResponse> addCart(@RequestBody CartRequest request);

    @PostMapping("/get-cart")
    ResponseEntity<List<Cart>> getCart(@RequestBody CartUserRequest request);
}
