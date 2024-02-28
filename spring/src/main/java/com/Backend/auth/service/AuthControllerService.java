package com.Backend.auth.service;

import com.Backend.auth.dto.LoginRequest;
import com.Backend.auth.dto.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RequestMapping(path = "/api/v1/auth")
public interface AuthControllerService {
    @PostMapping("/login")
    LoginResponse login(@RequestBody LoginRequest request);
}
