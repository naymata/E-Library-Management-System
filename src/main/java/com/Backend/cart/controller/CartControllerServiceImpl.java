package com.Backend.cart.controller;

import com.Backend.cart.dto.CartRequest;
import com.Backend.cart.dto.CartResponse;
import com.Backend.cart.dto.CartUserRequest;
import com.Backend.cart.dto.CartUserResponse;
import com.Backend.cart.exceptions.CartUserNotFoundException;
import com.Backend.cart.model.Cart;
import com.Backend.cart.service.CartControllerService;
import com.Backend.cart.service.CartService;
import com.Backend.user.exceptions.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class CartControllerServiceImpl implements CartControllerService {

    private final CartService service;
    @Override
    public CartResponse addCart(CartRequest request) {
        return null;
    }

    @Override
    public CartUserResponse getCart(CartUserRequest request){
        try {
            return service.getCart(request);
        }catch (UserNotFoundException | CartUserNotFoundException e){
            return new CartUserResponse(BAD_REQUEST.value(), new ArrayList<>());
        }catch (Exception e){
            return new CartUserResponse(INTERNAL_SERVER_ERROR.value(), new ArrayList<>());
        }
    }
}
