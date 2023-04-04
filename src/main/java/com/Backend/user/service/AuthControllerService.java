package com.Backend.user.service;

import com.Backend.user.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RequestMapping(path = "/api/v1/auth")
public interface AuthControllerService {


    @PostMapping("/sign-up")
    ResponseEntity<UserResponse> signUp(@RequestBody RegisterRequest request);

    @GetMapping("/account-verification/{token}")
    ResponseEntity<UserResponse> verifyAccount(@PathVariable String token);

    @PostMapping("/add-admin")
    ResponseEntity<UserResponse> addAdmin(@RequestBody UserRequest request);

    @PostMapping("/login")
    ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request);

    @PutMapping("/update")
    ResponseEntity<UserResponse> update(@RequestBody UpdateUserRequest request);

    @DeleteMapping("/delete/{username}")
    ResponseEntity<UserResponse> deleteUser(@PathVariable("username") String username);

    @GetMapping("/get-admins")
    ResponseEntity<ListAdminResponse> getOnlyAdmins();
}
