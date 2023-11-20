package com.Backend.user.implementation;

import com.Backend.user.dto.UserResponse;
import com.Backend.user.exceptions.UserNotFoundException;
import com.Backend.user.model.User;
import com.Backend.user.model.VerificationToken;
import com.Backend.user.repository.UserRepository;
import com.Backend.user.repository.VerificationTokenRepository;
import com.Backend.user.service.AccountVerificationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class AccountVerificationServiceImpl implements AccountVerificationService {

    @Autowired
    VerificationTokenRepository tokenRepository;
    @Autowired
    UserRepository userRepository;

    @PersistenceContext
    EntityManager manager;

    @Override
    @Transactional
    public UserResponse verifyAccount(String token) throws UserNotFoundException {

        Optional<VerificationToken> verificationToken = tokenRepository.findByToken(token);
        if (verificationToken.isPresent()) {
            fetchUserAndEnable(verificationToken.get());
            var user = userRepository.findByUsername(verificationToken.get().getUser().getUsername()).orElseThrow();
            if (user.getIsEnabled()) {
                deleteTokenAfterEnable(verificationToken.get());
            }
            return new UserResponse("Account activated");
        }
        return new UserResponse("\"Something wrong with token\" + \"\\nContact admin\"");
    }

    private void fetchUserAndEnable(VerificationToken token) throws UserNotFoundException {
        User user = userRepository.findByUsername(token.getUser().getUsername())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        user.setIsAccountNonLocked(true);
        user.setIsAccountNonExpired(true);
        user.setIsCredentialsNonExpired(true);
        user.setIsEnabled(true);
        userRepository.save(user);
    }

    private void deleteTokenAfterEnable(VerificationToken token) {
        manager.detach(token);
        token.setUser(null);
        tokenRepository.delete(token);
    }

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
