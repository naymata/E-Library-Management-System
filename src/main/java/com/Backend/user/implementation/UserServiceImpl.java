package com.Backend.user.implementation;

import com.Backend.config.JwtService;
import com.Backend.user.dto.*;
import com.Backend.user.model.NotificationEmail;
import com.Backend.user.model.Role;
import com.Backend.user.model.User;
import com.Backend.user.repository.UserRepository;
import com.Backend.user.service.AccountVerificationService;
import com.Backend.user.service.UserService;
import com.Backend.utility.ELibraryUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final AccountVerificationService accountVerificationService;
    private final MailServiceImpl mailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public UserResponse signUp(RegisterRequest request) {
        log.info("{}", request);
        if (repository.findByUsername(request.username()).isPresent()) {
            return new UserResponse("Username is taken");
        } else {
            if (repository.findByEmail(request.email()).isPresent()) {
                return new UserResponse("Email is taken");
            } else {
                if (checkIfEmailIsValid(request.email()) && checkIfPhoneNumberIsValid(request.phoneNumber())) {
                    var user = createUser(request);
                    sendToken(user);
                    repository.save(user);
                    return new UserResponse("Account " + "Created");
                } else {
                    return new UserResponse(ELibraryUtility.INVALID_INFORMATION);
                }
            }
        }
    }

    @Override
    public UserResponse addAdmin(UserRequest request) {
        log.info("{}", request);
        if (!request.username().isEmpty() && !request.password().isEmpty()) {
            repository.save(createAdmin(request));
            return new UserResponse("Admin " + "Created");
        }
        return new UserResponse(ELibraryUtility.SOMETHING_WENT_WRONG);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        var user = repository.findByUsername(request.username()).orElseThrow();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                ));
        var jwtToken = jwtService.generateToken(user);

        return new LoginResponse(jwtToken,
                Instant.now().plusMillis(JwtService.TimeMillis),
                user.getUsername(),
                user.getRole());
    }


    @Override
    public UserResponse updateUser(UpdateUserRequest request) {
        var user = repository.findByUsername(request.username()).orElseThrow();
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPhoneNumber(request.phoneNumber());
        repository.save(user);
        return new UserResponse("User" + ELibraryUtility.UPDATED_SUCCESSFULLY);
    }

    @Override
    public UserResponse deleteUser(String username) {
        var user = repository.findByUsername(username).orElseThrow();
        repository.deleteById(user.getId());
        return new UserResponse("Created");
    }

    @Override
    public ListAdminResponse onlyAdmins() {
        var list = repository.findByRole(Role.ADMIN).orElseThrow();
        return new ListAdminResponse(list);

    }

    private User createAdmin(UserRequest request) {
        return User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .isEnabled(true)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .role(Role.ADMIN)
                .build();
    }

    private User createUser(RegisterRequest request) {
        return User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .phoneNumber(request.phoneNumber())
                .isEnabled(false)
                .isAccountNonExpired(false)
                .isAccountNonExpired(false)
                .isCredentialsNonExpired(false)
                .isAccountNonLocked(false)
                .role(Role.CLIENT)
                .build();
    }

    private Boolean checkIfEmailIsValid(String email) {
        return email.contains("@") && email.contains(".");
    }

    private Boolean checkIfPhoneNumberIsValid(String phoneNumber) {
        if (phoneNumber.length() == 13) {
            return checkIfNumberContainsOnlyNumeric(phoneNumber);
        }

        return false;
    }

    private Boolean checkIfNumberContainsOnlyNumeric(String phoneNumber) {
        if (phoneNumber.charAt(0) == '+') {
            return phoneNumber.chars().anyMatch(Character::isDigit);
        }
        return false;
    }


    private NotificationEmail sendEmailVerificationToken(final String email, final String token) {
        return new NotificationEmail(
                "Please Activate your Account: "
                , email,
                "Thank you for signing up to E-Library "
                        + "please click the link: "
                        + "<a href="
                        + "http://localhost:8080/api/v1/auth/account-verification/"
                        + token
                        + ">"
                        + "Activate"
                        + "</a>"
                        + "\t so you can activate your account"
        );
    }
    private void sendToken(User user) {
        var token = accountVerificationService.generateVerificationToken(user);
        mailService.sendEmail(sendEmailVerificationToken(user.getEmail(), token));
    }
}
