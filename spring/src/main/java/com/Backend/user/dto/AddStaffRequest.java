package com.Backend.user.dto;

import com.Backend.user.model.Role;

public record AddStaffRequest(
        String username,
        String password,
        Role role
) {
}
