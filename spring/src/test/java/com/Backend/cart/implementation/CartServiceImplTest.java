package com.Backend.cart.implementation;

import com.Backend.book.exceptions.BookNotFoundException;
import com.Backend.book.exceptions.BookQuantityException;
import com.Backend.book.model.Book;
import com.Backend.book.repository.BookRepository;
import com.Backend.cart.dto.AddCartRequest;
import com.Backend.cart.dto.CartUserRequest;
import com.Backend.cart.exceptions.CartUserNotFoundException;
import com.Backend.cart.model.Cart;
import com.Backend.cart.repository.CartRepository;
import com.Backend.cart.service.CartService;
import com.Backend.user.exceptions.UserNotFoundException;
import com.Backend.user.model.User;
import com.Backend.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;

import static com.Backend.config.utility.ELibraryUtility.*;
import static com.Backend.config.utility.ELibraryUtility.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;

class CartServiceImplTest {
    @Mock
    private CartRepository repository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookRepository bookRepository;
    private CartService underTest;
    private static final String username = "username";
    private static final Long id = 1L;
    private static final Short quantity = 5, requestedQuantity = 3, invalidRequestedQuantity = 12;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new CartServiceImpl(repository, userRepository, bookRepository);
    }

    @Test
    @DisplayName("Should add card to client cart")
    public void shouldAddCartToClientCart() {
        User user = new User();
        user.setUsername(username);
        Book book = new Book();
        book.setQuantity(quantity);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        var response = underTest.addCart(new AddCartRequest(username, id, requestedQuantity));

        verify(repository, times(1)).save(any(Cart.class));
        assertEquals(OK.value(), response.status());
        assertEquals(stringFormatter("Book added", " successfully", "to cart"), response.message());
    }

    @Test
    @DisplayName("Should throw BookNotFoundException when given wrong id")
    public void itShould_Throw_BookNotFoundException_When_Given_WrongId() {
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> underTest.addCart(new AddCartRequest(username, id, requestedQuantity)))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining(stringFormatter("Book with id:", 1L, NOT_FOUND));
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when given wrong username")
    public void itShould_Throw_UserNotFoundException_When_Given_WrongUsername() {
        Book book = new Book();
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        assertThatThrownBy(() -> underTest.addCart(new AddCartRequest(username, id, requestedQuantity)))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(stringFormatter("User with username:", username, NOT_FOUND));
    }

    @Test
    @DisplayName("Should throw BookQuantityException when given wrong quantity")
    public void itShould_Throw_BookQuantityException_When_Given_WrongQuantity() {
        User user = new User();
        user.setUsername(username);
        Book book = new Book();
        book.setQuantity(quantity);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        assertThatThrownBy(() -> underTest.addCart(new AddCartRequest(username, id, invalidRequestedQuantity)))
                .isInstanceOf(BookQuantityException.class)
                .hasMessageContaining("Insufficient quantity in stock");
    }

    @Test
    @DisplayName("Should get client cart")
    public void shouldGetClientCart() {
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(repository.findByUser(user)).thenReturn(Optional.of(new ArrayList<>()));

        var response = underTest.getCart(new CartUserRequest(username));

        assertEquals(OK.value(), response.status());
        assertEquals(SUCCESS, response.message());
        assertEquals(new ArrayList<>(), response.data());
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when given wrong username")
    public void itShould_Throw_UserNotFoundException_When_Given_WrongUsername_When_AskingForCart() {
        assertThatThrownBy(() -> underTest.getCart(new CartUserRequest(username)))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(stringFormatter("User with username:", username, NOT_FOUND));
    }
    @Test
    @DisplayName("Should Throw CartUserNotFound when user does not have cart")
    public void itShould_Throw_CartUserNotFoundException_When_UserDoesNotHaveCart(){
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> underTest.getCart(new CartUserRequest(username)))
                .isInstanceOf(CartUserNotFoundException.class)
                .hasMessageContaining("Cart does not exist");
    }
}