package com.Backend.user.dto;

public record UserRequest(
        String username,
        String password
) {
}
