package com.Backend.auth.implementation;

import com.Backend.auth.dto.LoginRequest;
import com.Backend.auth.dto.LoginResponse;
import com.Backend.auth.service.AuthenticationService;
import com.Backend.config.security.Jwt.JwtService;
import com.Backend.user.dto.UserDTO;
import com.Backend.user.exceptions.UserNotFoundException;
import com.Backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import static com.Backend.config.utility.ELibraryUtility.*;

/**
 * Service implementation for user authentication.
 * Handles user login functionality using Spring Security's AuthenticationManager
 * and generates JWT tokens for authenticated users.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository repository;
    private final JwtService jwtService;

    /**
     * Performs user login by authenticating the provided credentials and generates a JWT token upon successful authentication.
     *
     * @param request The login request containing the username and password.
     * @return A {@link LoginResponse} containing the authentication status, JWT token, and user information.
     * @throws UserNotFoundException if the user with the provided username is not found.
     */
    @Override
    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                ));
        var user = repository.findByUsername(request.username())
                .orElseThrow(() -> new UserNotFoundException(stringFormatter("User with username:", request.username(), NOT_FOUND)));
        var userDTO = new UserDTO(user.getId(), user.getUsername(), user.getPassword(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhoneNumber());
        var token = jwtService.generateToken(user);
        return new LoginResponse(HttpStatus.OK.value(), SUCCESS, token, userDTO);
    }
}
