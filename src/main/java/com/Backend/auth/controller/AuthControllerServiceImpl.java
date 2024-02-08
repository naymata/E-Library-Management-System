package com.Backend.auth.controller;

import com.Backend.auth.dto.LoginRequest;
import com.Backend.auth.dto.LoginResponse;
import com.Backend.auth.service.AuthControllerService;
import com.Backend.auth.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RestController;

import static com.Backend.config.utility.ELibraryUtility.*;

@RestController
@RequiredArgsConstructor
public class AuthControllerServiceImpl implements AuthControllerService {

    private final AuthenticationService service;
    private static final Logger logger = LoggerFactory.getLogger(AuthControllerServiceImpl.class);
    /**
     * Handles the login request by delegating the authentication process to the
     * {@link AuthenticationService}.
     *
     * @param request The login request containing user credentials.
     * @return A {@link LoginResponse} containing the authentication result.
     */
    @Override
    public LoginResponse login(LoginRequest request) {
        try {
            return service.login(request);
        } catch (AuthenticationException e) {
            return new LoginResponse(HttpStatus.BAD_REQUEST.value(), "Invalid credentials", null, null);
        } catch (RuntimeException e) {
            logger.error(e.getMessage(),e.fillInStackTrace());
            return new LoginResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), SOMETHING_WENT_WRONG, null, null);
        }
    }

}
