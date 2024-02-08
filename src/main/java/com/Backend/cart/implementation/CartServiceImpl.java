package com.Backend.cart.implementation;

import com.Backend.book.exceptions.BookNotFoundException;
import com.Backend.book.exceptions.BookQuantityException;
import com.Backend.book.model.Book;
import com.Backend.book.repository.BookRepository;
import com.Backend.cart.dto.AddCartRequest;
import com.Backend.cart.dto.AddCartResponse;
import com.Backend.cart.dto.CartUserRequest;
import com.Backend.cart.dto.CartUserResponse;
import com.Backend.cart.exceptions.CartUserNotFoundException;
import com.Backend.cart.model.Cart;
import com.Backend.cart.repository.CartRepository;
import com.Backend.cart.service.CartService;
import com.Backend.user.exceptions.UserNotFoundException;
import com.Backend.user.model.User;
import com.Backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

import static com.Backend.config.utility.ELibraryUtility.*;
import static com.Backend.config.utility.ELibraryUtility.NOT_FOUND;
import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {


    private final CartRepository repository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    /**
     * Adds a book to the user's cart based on the provided request.
     *
     * @param request The request containing information about the user, book, and purchased quantity.
     * @return The response indicating the success of adding the book to the cart.
     * @throws UserNotFoundException      if the user with the specified username is not found.
     * @throws BookNotFoundException      if the book with the specified ID is not found.
     * @throws BookQuantityException      if the purchased quantity is greater than the available quantity in stock.
     */
    @Override
    public AddCartResponse addCart(AddCartRequest request) {
        var user = userRepository.findByUsername(request.username()).orElseThrow(() ->
                new UserNotFoundException(stringFormatter("User with username:", request.username(), NOT_FOUND)));

        var book = bookRepository.findById(request.bookId()).orElseThrow(() ->
                new BookNotFoundException(stringFormatter("Book with id:", request.bookId(), NOT_FOUND)));
        if (book.getQuantity() < request.purchasedQuantity()) {
            throw new BookQuantityException("Insufficient quantity in stock");
        }

        var cart = createCart(user, book);
        repository.save(cart);
        return new AddCartResponse(OK.value(), stringFormatter("Book added", " successfully", "to cart"));
    }
    /**
     * Retrieves the cart of the specified user.
     *
     * @param request The request containing information about the user.
     * @return The response containing the user's cart data.
     * @throws UserNotFoundException      if the user with the specified username is not found.
     * @throws CartUserNotFoundException  if the cart for the specified user does not exist.
     */
    @Override
    public CartUserResponse getCart(CartUserRequest request) {
        var user = userRepository.findByUsername(request.username()).orElseThrow(
                () -> new UserNotFoundException(stringFormatter("User with username:", request.username(), NOT_FOUND)
                ));
        var data = repository.findByUser(user).orElseThrow(
                () -> new CartUserNotFoundException("Cart does not exist")
        );
        return new CartUserResponse(OK.value(), SUCCESS, data);
    }

    /**
     * Creates a new cart entry for the specified user and book.
     *
     * @param user The user associated with the cart entry.
     * @param book The book associated with the cart entry.
     * @return The newly created cart entry.
     */
    private Cart createCart(User user, Book book) {
        return Cart
                .builder()
                .user(user)
                .book(book)
                .purchasedOn(Date.from(Instant.now()))
                .build();
    }

}
