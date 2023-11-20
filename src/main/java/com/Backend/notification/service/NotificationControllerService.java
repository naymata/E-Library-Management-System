package com.Backend.notification.service;


import com.Backend.notification.dto.NotificationDataResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(path = "/api/v1/notification")
public interface NotificationControllerService {

    @GetMapping("/get-notifications")
    NotificationDataResponse getNotifications();
}
