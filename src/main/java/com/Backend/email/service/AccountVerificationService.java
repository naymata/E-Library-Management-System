package com.Backend.email.service;

import com.Backend.email.dto.AccountVerificationResponse;
import com.Backend.user.model.User;

public interface AccountVerificationService {
    AccountVerificationResponse verifyAccount(String token);
    String generateVerificationToken(User user);
}
