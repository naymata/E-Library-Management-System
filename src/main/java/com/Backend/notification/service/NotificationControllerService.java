package com.Backend.notification.service;


import com.Backend.notification.dto.NotificationPaginationRequest;
import com.Backend.notification.dto.NotificationPaginationResponse;
import com.Backend.notification.dto.UpdateNotificationResponse;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = "/api/v1/notification")
public interface NotificationControllerService {

    @PatchMapping("/update{id}")
    UpdateNotificationResponse updateNotification(@PathVariable(name = "id") Long id);

    @GetMapping("/get-notifications")
    NotificationPaginationResponse getNotifications(@RequestBody NotificationPaginationRequest request);
}
