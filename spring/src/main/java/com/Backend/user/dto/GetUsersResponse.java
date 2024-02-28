package com.Backend.user.dto;

import com.Backend.user.model.User;

import java.util.List;

public record GetUsersResponse(
        Integer status,
        String message,
        Integer page,
        List<User> data
) {
}
