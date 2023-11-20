package com.Backend.cart.service;

import com.Backend.book.exceptions.BookNotFoundException;
import com.Backend.book.exceptions.BookQuantityException;
import com.Backend.cart.dto.CartRequest;
import com.Backend.cart.dto.CartResponse;
import com.Backend.cart.dto.CartUserRequest;
import com.Backend.cart.dto.CartUserResponse;
import com.Backend.cart.exceptions.CartUserNotFoundException;
import com.Backend.cart.model.Cart;
import com.Backend.user.exceptions.UserNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CartService {

    CartResponse addCart(@RequestBody CartRequest request) throws BookNotFoundException, UserNotFoundException, BookQuantityException;
    CartUserResponse getCart(@RequestBody CartUserRequest request) throws UserNotFoundException, CartUserNotFoundException;
}
