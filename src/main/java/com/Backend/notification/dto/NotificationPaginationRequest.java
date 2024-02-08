package com.Backend.notification.dto;


import com.Backend.notification.enums.NotificationType;

public record NotificationPaginationRequest(
        Integer page,
        Integer size,
        NotificationType notificationType
) {
    public NotificationPaginationRequest {
        if (size == null) {
            size = 4;
        }
        if (page == null) {
            page = 0;
        }
        if (notificationType == null) {
            notificationType = NotificationType.UN_READ;
        }
    }
}
