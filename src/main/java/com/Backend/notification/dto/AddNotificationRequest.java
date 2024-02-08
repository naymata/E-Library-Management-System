package com.Backend.notification.dto;

public record AddNotificationRequest(
        Long bookId,
        String message
) {
}
