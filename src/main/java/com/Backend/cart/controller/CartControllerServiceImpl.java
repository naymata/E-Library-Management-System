package com.Backend.cart.controller;

import com.Backend.cart.dto.CartRequest;
import com.Backend.cart.dto.CartResponse;
import com.Backend.cart.dto.CartUserRequest;
import com.Backend.cart.model.Cart;
import com.Backend.cart.service.CartControllerService;
import com.Backend.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CartControllerServiceImpl implements CartControllerService {

    private final CartService service;
    @Override
    public ResponseEntity<CartResponse> addCart(CartRequest request) {
        return ResponseEntity.ok(service.addCart(request));
    }

    @Override
    public ResponseEntity<List<Cart>> getCart(CartUserRequest request) {
        return ResponseEntity.ok(service.getCart(request));
    }
}
