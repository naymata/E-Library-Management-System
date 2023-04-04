package com.Backend.cart.repository;

import com.Backend.cart.model.Cart;
import com.Backend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<List<Cart>> findByUser(User user);
}
