package com.Backend.user.dto;

import com.Backend.user.model.Role;

public record GetUsersRequest(
        Integer page,
        Integer size,
        Role role
) {
    public GetUsersRequest {

        if (size == null) {
            size = 4;
        }
        if (page == null) {
            page = 0;
        }
        if (role == null) {
            role = Role.EMPLOYEE;
        }
    }
}
