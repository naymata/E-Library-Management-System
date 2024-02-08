package com.Backend.notification.controller;

import com.Backend.notification.dto.NotificationPaginationRequest;
import com.Backend.notification.dto.NotificationPaginationResponse;
import com.Backend.notification.dto.UpdateNotificationResponse;
import com.Backend.notification.exceptions.NotificationNotFoundException;
import com.Backend.notification.exceptions.NotificationPageException;
import com.Backend.notification.exceptions.NotificationSizeException;
import com.Backend.notification.exceptions.NotificationTypeException;
import com.Backend.notification.service.NotificationControllerService;
import com.Backend.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import static com.Backend.config.utility.ELibraryUtility.*;
import static org.springframework.http.HttpStatus.*;

@RequiredArgsConstructor
public class NotificationControllerServiceImpl implements NotificationControllerService {

    private final NotificationService service;
    private static final Logger logger = LoggerFactory.getLogger(NotificationControllerServiceImpl.class);

    @Override
    public UpdateNotificationResponse updateNotification(Long id) {
        try {
            return service.updateNotification(id);
        } catch (NotificationNotFoundException e) {
            return new UpdateNotificationResponse(BAD_REQUEST.value(), e.getMessage());
        } catch (RuntimeException e) {
            logger.error(e.getMessage(),e.fillInStackTrace());
            return new UpdateNotificationResponse(INTERNAL_SERVER_ERROR.value(), SOMETHING_WENT_WRONG);
        }
    }

    @Override
    public NotificationPaginationResponse getNotifications(NotificationPaginationRequest request) {
        try {
            return service.getNotifications(request);
        } catch (NotificationSizeException | NotificationPageException | NotificationTypeException e) {
            return new NotificationPaginationResponse(BAD_REQUEST.value(), e.getMessage(), new ArrayList<>());
        } catch (RuntimeException e) {
            logger.error(e.getMessage(),e.fillInStackTrace());
            return new NotificationPaginationResponse(INTERNAL_SERVER_ERROR.value(), SOMETHING_WENT_WRONG, new ArrayList<>());
        }
    }
}
