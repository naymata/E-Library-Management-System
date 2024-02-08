package com.Backend.email.controller;

import com.Backend.email.dto.AccountVerificationResponse;
import com.Backend.user.exceptions.UserNotFoundException;
import com.Backend.email.exceptions.VerificationTokenNotFoundException;
import com.Backend.email.service.AccountVerificationService;
import com.Backend.email.service.VerificationControllerService;
import com.Backend.config.utility.ELibraryUtility;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class VerificationControllerServiceImpl implements VerificationControllerService {
    private final AccountVerificationService service;
    private static final Logger logger = LoggerFactory.getLogger(VerificationControllerServiceImpl.class);


    /**
     * Performs the verification of a user account using the provided verification token.
     * This method delegates the verification process to the {@link AccountVerificationService}.
     *
     * @param token The verification token associated with the user account to be verified.
     * @return An {@link AccountVerificationResponse} indicating the result of the account verification.
     * @throws VerificationTokenNotFoundException if the provided token is not found.
     * @throws UserNotFoundException if the user associated with the provided token is not found.
     */
    @Override
    public AccountVerificationResponse verifyAccount(String token) {
        try {
            return service.verifyAccount(token);
        } catch (VerificationTokenNotFoundException | UserNotFoundException e) {
            return new AccountVerificationResponse(e.getMessage());
        } catch (RuntimeException e) {
            logger.error(e.getMessage(),e.fillInStackTrace());
            return new AccountVerificationResponse(ELibraryUtility.SOMETHING_WENT_WRONG);
        }
    }

    @Override
    public String hi() {
        return "hello";
    }
}
