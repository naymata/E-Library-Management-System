package com.Backend.cart.implementation;

import com.Backend.book.exceptions.BookNotFoundException;
import com.Backend.book.exceptions.BookQuantityException;
import com.Backend.book.model.Book;
import com.Backend.book.repository.BookRepository;
import com.Backend.cart.dto.CartRequest;
import com.Backend.cart.dto.CartResponse;
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

import static com.Backend.utility.ELibraryUtility.*;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {


    private final CartRepository repository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Override
    public CartResponse addCart(CartRequest request) throws BookNotFoundException, UserNotFoundException, BookQuantityException {
        var book = bookRepository.findById(request.bookId()).orElseThrow(() ->
                new BookNotFoundException("Book with id: " + request.bookId() + " " + NOT_FOUND));

        var user = userRepository.findByUsername(request.username()).orElseThrow(() ->
                new UserNotFoundException("User with username: " + request.username() + " " + NOT_FOUND));

        if (book.getQuantity() < request.purchasedAmount()) {
            throw new BookQuantityException("Not enough books");
        }
        var cart = createCart(user, book);
        repository.save(cart);
        book.setQuantity(bookIsPurchased(book, request.purchasedAmount()));
        bookRepository.save(book);
        return new CartResponse(200, "Book successfully purchased");
    }

    @Override
    public CartUserResponse getCart(CartUserRequest request) throws UserNotFoundException, CartUserNotFoundException {
        var user = userRepository.findByUsername(request.username()).orElseThrow(
                () -> new UserNotFoundException("User with username: " + request.username() + " " + NOT_FOUND)
        );
        var data = repository.findByUser(user).orElseThrow(
                () -> new CartUserNotFoundException("Cart does not exist")
        );
        return new CartUserResponse(200, data);
    }


    private Cart createCart(User user, Book book) {
        return Cart
                .builder()
                .user(user)
                .book(book)
                .purchasedOn(Date.from(Instant.now()))
                .build();
    }

    private Short bookIsPurchased(Book book, Short quantity) {
        return (short) (book.getQuantity() - quantity);
    }
}
