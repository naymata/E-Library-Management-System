package com.Backend.cart.controller;

import com.Backend.book.exceptions.BookNotFoundException;
import com.Backend.book.exceptions.BookQuantityException;
import com.Backend.cart.dto.AddCartRequest;
import com.Backend.cart.dto.AddCartResponse;
import com.Backend.cart.dto.CartUserRequest;
import com.Backend.cart.dto.CartUserResponse;
import com.Backend.cart.exceptions.CartUserNotFoundException;
import com.Backend.cart.service.CartControllerService;
import com.Backend.cart.service.CartService;
import com.Backend.user.exceptions.UserNotFoundException;
import com.Backend.config.utility.ELibraryUtility;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

import static org.springframework.http.HttpStatus.*;
@RestController
@RequiredArgsConstructor
public class CartControllerServiceImpl implements CartControllerService {

    private final CartService service;
    private static final Logger logger = LoggerFactory.getLogger(CartControllerServiceImpl.class);
    /**
     * Adds a book to the user's cart based on the information provided in the {@link AddCartRequest}.
     *
     * @param request The {@link AddCartRequest} containing details of the book to be added to the cart.
     * @return An {@link AddCartResponse} indicating the result of the addition to the cart operation.
     * @throws UserNotFoundException if the user associated with the cart is not found.
     * @throws BookNotFoundException if the specified book is not found.
     * @throws BookQuantityException if the specified book quantity is invalid.
     */
    @Override
    public AddCartResponse addCart(AddCartRequest request) {
        try {
            return service.addCart(request);
        } catch (UserNotFoundException | BookNotFoundException | BookQuantityException e) {
            return new AddCartResponse(BAD_REQUEST.value(), e.getMessage());
        } catch (RuntimeException e) {
            logger.error(e.getMessage(),e.fillInStackTrace());
            return new AddCartResponse(INTERNAL_SERVER_ERROR.value(), ELibraryUtility.SOMETHING_WENT_WRONG);
        }
    }
    /**
     * Retrieves the contents of the user's cart based on the information provided in the {@link CartUserRequest}.
     *
     * @param request The {@link CartUserRequest} containing details of the user's cart to be retrieved.
     * @return A {@link CartUserResponse} indicating the result of the cart retrieval operation.
     * @throws UserNotFoundException if the user associated with the cart is not found.
     * @throws CartUserNotFoundException if the user's cart is not found.
     */
    @Override
    public CartUserResponse getCart(CartUserRequest request) {
        try {
            return service.getCart(request);
        } catch (UserNotFoundException | CartUserNotFoundException e) {
            return new CartUserResponse(BAD_REQUEST.value(), e.getMessage(), new ArrayList<>());
        } catch (RuntimeException e) {
            logger.error(e.getMessage(),e.fillInStackTrace());
            return new CartUserResponse(INTERNAL_SERVER_ERROR.value(), ELibraryUtility.SOMETHING_WENT_WRONG, new ArrayList<>());
        }
    }
}
