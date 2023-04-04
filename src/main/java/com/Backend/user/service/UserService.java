package com.Backend.user.service;

import com.Backend.user.dto.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;


public interface UserService {
    UserResponse signUp(@RequestBody RegisterRequest request);

    UserResponse addAdmin(@RequestBody UserRequest request);

    LoginResponse login(@RequestBody LoginRequest request);

    UserResponse updateUser(@RequestBody UpdateUserRequest request);

    UserResponse deleteUser(@PathVariable String username);

    ListAdminResponse onlyAdmins();

}
