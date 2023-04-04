package com.Backend.cart.implementation;

import com.Backend.book.model.Book;
import com.Backend.book.repository.BookRepository;
import com.Backend.cart.dto.CartRequest;
import com.Backend.cart.dto.CartResponse;
import com.Backend.cart.dto.CartUserRequest;
import com.Backend.cart.exceptions.CartExceptions;
import com.Backend.cart.model.Cart;
import com.Backend.cart.repository.CartRepository;
import com.Backend.cart.service.CartService;
import com.Backend.user.model.User;
import com.Backend.user.repository.UserRepository;
import com.Backend.utility.ELibraryUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {


    private final CartRepository repository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @Override
    public CartResponse addCart(CartRequest request) {
        try {
            var user = userRepository.findByUsername(request.username());
            var book = bookRepository.findById(request.bookId());
            if (book.isPresent() && user.isPresent()) {
                var cart = createCart(user.get(), book.get());
                repository.save(cart);
                return new CartResponse("Cart " + ELibraryUtility.CREATED);
            }
            return new CartResponse(ELibraryUtility.INVALID_INFORMATION);
        } catch (Exception e) {
            throw new CartExceptions(e.getMessage(), e.getCause());
        }
    }

    @Override
    public List<Cart> getCart(CartUserRequest request) {
        try {
            var user = userRepository.findByUsername(request.username()).orElseThrow();
            if (repository.findByUser(user).isPresent()) {
                return repository.findByUser(user).get();
            }
            return new ArrayList<>();
        } catch (Exception e) {
            throw new CartExceptions(e.getMessage(), e.getCause());
        }
    }


    private Cart createCart(User user, Book book) {
        return Cart
                .builder()
                .user(user)
                .book(book)
                .purchasedOn(Date.from(Instant.now()))
                .build();
    }

}
