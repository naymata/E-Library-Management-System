package com.Backend.user.dto;

public record UpdateUserRequest(
	String username,
        String password,
        String firstName,
        String lastName,
        String phoneNumber
) {
}
