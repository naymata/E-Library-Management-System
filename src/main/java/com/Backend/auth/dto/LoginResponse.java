package com.Backend.auth.dto;

import com.Backend.user.dto.UserDTO;

public record LoginResponse(
        Integer status,
        String message,
        String token,
        UserDTO userDTO
) {
}
