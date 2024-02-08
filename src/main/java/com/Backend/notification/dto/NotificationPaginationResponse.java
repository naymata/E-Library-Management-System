package com.Backend.notification.dto;

import com.Backend.notification.model.Notification;

import java.util.List;

public record NotificationPaginationResponse(
        Integer status,
        String message,
        List<Notification> data
) {
}
