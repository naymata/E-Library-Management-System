package com.Backend.user.service;

import com.Backend.user.dto.UserResponse;
import com.Backend.user.exceptions.UserNotFoundException;
import com.Backend.user.model.User;

public interface AccountVerificationService {
    UserResponse verifyAccount(String token) throws UserNotFoundException;
    String generateVerificationToken(User user);
}
