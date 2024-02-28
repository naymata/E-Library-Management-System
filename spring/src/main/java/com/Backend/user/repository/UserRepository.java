package com.Backend.user.repository;

import com.Backend.user.model.Role;
import com.Backend.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    Page<User> getUserByRole(Pageable pageable, Role role);
}
