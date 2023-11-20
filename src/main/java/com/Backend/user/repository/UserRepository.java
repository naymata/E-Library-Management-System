package com.Backend.user.repository;

import com.Backend.user.model.Role;
import com.Backend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<List<User>> findByRole(Role role);

    Boolean existsByUsername(String username);
}
