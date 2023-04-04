package com.Backend.user.dto;

import com.Backend.user.model.Role;
import java.time.Instant;


public record LoginResponse(
        String authenticationToken,
        Instant expiresAt,
        String username,
        Role role
) {

}
