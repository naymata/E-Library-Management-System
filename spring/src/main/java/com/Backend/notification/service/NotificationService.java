package com.Backend.notification.service;

import com.Backend.notification.dto.*;
import com.Backend.notification.model.Notification;

import java.util.List;

public interface NotificationService {

    void addNotification(AddNotificationRequest request);
    UpdateNotificationResponse updateNotification(Long id);

    NotificationPaginationResponse getNotifications(NotificationPaginationRequest request);

}
