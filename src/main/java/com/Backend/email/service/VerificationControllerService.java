package com.Backend.email.service;

import com.Backend.email.dto.AccountVerificationResponse;
import com.Backend.email.dto.TestResquest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(path = "/api/v1/verification")
public interface VerificationControllerService {
    @GetMapping("/{token}")
    AccountVerificationResponse verifyAccount(@PathVariable String token);

    @GetMapping("/hi")
    String hi();
}
