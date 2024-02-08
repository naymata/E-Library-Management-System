package com.Backend.email.implementation;

import com.Backend.email.dto.AccountVerificationResponse;
import com.Backend.user.exceptions.UserNotFoundException;
import com.Backend.email.exceptions.VerificationTokenNotFoundException;
import com.Backend.user.model.User;
import com.Backend.email.model.VerificationToken;
import com.Backend.user.repository.UserRepository;
import com.Backend.email.repository.VerificationTokenRepository;
import com.Backend.email.service.AccountVerificationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

import static com.Backend.config.utility.ELibraryUtility.*;

@Service
public class AccountVerificationServiceImpl implements AccountVerificationService {

    @Autowired
    VerificationTokenRepository tokenRepository;
    @Autowired
    UserRepository userRepository;

    @PersistenceContext
    EntityManager manager;
    /**
     * Verifies the user account using the provided verification token.
     *
     * @param token The verification token.
     * @return The response indicating the result of the account verification.
     * @throws VerificationTokenNotFoundException If the verification token is not found.
     */
    @Override
    @Transactional
    public AccountVerificationResponse verifyAccount(String token) {
        var verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new VerificationTokenNotFoundException(stringFormatter("Verification token:", token, NOT_FOUND)));
        fetchUserAndEnable(verificationToken);
        var user = userRepository.findByUsername(verificationToken.getUser().getUsername()).orElseThrow();
        if (user.getIsEnabled()) {
            deleteTokenAfterEnable(verificationToken);
        }
        return new AccountVerificationResponse("Account activated");
    }
    /**
     * Fetches the user associated with the verification token and enables the user account.
     *
     * @param token The verification token.
     * @throws UserNotFoundException If the user associated with the token is not found.
     */
    private void fetchUserAndEnable(VerificationToken token){
        User user = userRepository.findByUsername(token.getUser().getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setIsAccountNonLocked(true);
        user.setIsAccountNonExpired(true);
        user.setIsCredentialsNonExpired(true);
        user.setIsEnabled(true);
        userRepository.save(user);
    }
    /**
     * Deletes the verification token after enabling the user account.
     *
     * @param token The verification token to be deleted.
     */
    private void deleteTokenAfterEnable(VerificationToken token) {
        manager.detach(token);
        token.setUser(null);
        tokenRepository.delete(token);
    }
    /**
     * Generates a verification token for the specified user.
     *
     * @param user The user for whom the verification token is generated.
     * @return The generated verification token.
     */
    @Override
    public String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken
                .builder()
                .user(user)
                .token(token)
                .build();
        tokenRepository.save(verificationToken);
        return token;
    }

}
