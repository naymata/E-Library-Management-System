package com.Backend.user.dto;

public record UpdateUserRequest(
        Long id,
        String password,
        String firstName,
        String lastName,
        String phoneNumber
) {
}
