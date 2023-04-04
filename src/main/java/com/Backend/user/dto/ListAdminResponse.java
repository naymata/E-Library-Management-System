package com.Backend.user.dto;

import com.Backend.user.model.User;
import java.util.List;

public record ListAdminResponse(
        List<User> listResponse
) {
}
