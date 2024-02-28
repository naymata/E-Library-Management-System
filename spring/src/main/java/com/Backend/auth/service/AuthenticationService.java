package com.Backend.auth.service;

import com.Backend.auth.dto.LoginRequest;
import com.Backend.auth.dto.LoginResponse;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthenticationService {

    LoginResponse login(LoginRequest request);
}
