package com.Backend.notification.dto;


public record NotificationPaginationRequest(
        Integer size,
        Integer page
) {
    public NotificationPaginationRequest {
        if (size == null) {
            size = 4;
        }
        if (page == null) {
            page = 0;
        }
    }
}
