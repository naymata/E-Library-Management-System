package com.Backend.user.controller;

import com.Backend.user.dto.*;
import com.Backend.user.exceptions.UserNotFoundException;
import com.Backend.user.service.AccountVerificationService;
import com.Backend.user.service.AuthControllerService;
import com.Backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthControllerServiceImpl implements AuthControllerService {

    private final UserService service;


    private final AccountVerificationService accountVerificationService;

    @Override
    public ResponseEntity<UserResponse> signUp(RegisterRequest request) {
        return ResponseEntity.ok(service.signUp(request));
    }

    @Override
    public ResponseEntity<UserResponse> verifyAccount(String token) throws UserNotFoundException {
        return ResponseEntity.ok(accountVerificationService.verifyAccount(token));
    }

    @Override
    public ResponseEntity<UserResponse> addAdmin(UserRequest request) {
        return ResponseEntity.ok(service.addAdmin(request));
    }

    @Override
    public ResponseEntity<LoginResponse> login(LoginRequest request) {
        return ResponseEntity.ok(service.login(request));
    }

    @Override
    public ResponseEntity<UserResponse> update(UpdateUserRequest request) {
        return ResponseEntity.ok(service.updateUser(request));
    }

    @Override
    public ResponseEntity<UserResponse> deleteUser(String username) {
        return ResponseEntity.ok(service.deleteUser(username));
    }

    @Override
    public ResponseEntity<ListAdminResponse> getOnlyAdmins() {
        return ResponseEntity.ok(service.onlyAdmins());
    }
}
