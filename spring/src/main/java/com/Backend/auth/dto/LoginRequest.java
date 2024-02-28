package com.Backend.auth.dto;

public record LoginRequest(
        String username,
        String password
) {
}
