package com.Backend.user.dto;

public record LoginRequest(
        String username,
        String password
) {
}
