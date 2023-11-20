package com.Backend.notification.service;

import com.Backend.notification.dto.AddNotificationRequest;
import com.Backend.notification.dto.NotificationPaginationRequest;
import com.Backend.notification.dto.NotificationDataResponse;

public interface NotificationService {
    void addNotification(AddNotificationRequest request);


    NotificationDataResponse getNotifications(NotificationPaginationRequest request);
}
