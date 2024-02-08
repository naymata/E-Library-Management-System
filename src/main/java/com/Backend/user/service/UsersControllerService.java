package com.Backend.user.service;

import com.Backend.user.dto.*;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = "/api/v1.1/users")
public interface UsersControllerService {

    @PostMapping("/register")
    RegisterResponse register(@RequestBody RegisterRequest request);

    @PutMapping("/update")
    UpdateUserResponse update(@RequestBody UpdateUserRequest request);

    @DeleteMapping("/delete/{id}")
    DeleteUserResponse deleteUser(@PathVariable("id") Long id);
}
