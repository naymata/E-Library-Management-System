package com.Backend.user.dto;

public record UserDTO(
        Long id,
        String username,
        String password,
        String firstName,
        String lastName,
        String email,
        String phoneNumber
) {
}
