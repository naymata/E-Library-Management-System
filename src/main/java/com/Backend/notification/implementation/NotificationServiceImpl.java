package com.Backend.notification.implementation;

import com.Backend.notification.dto.*;
import com.Backend.notification.exceptions.*;
import com.Backend.notification.model.Notification;
import com.Backend.notification.repository.NotificationRepository;
import com.Backend.notification.service.NotificationService;
import com.Backend.config.utility.ELibraryUtility;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.Backend.notification.enums.NotificationType.*;
import static com.Backend.config.utility.ELibraryUtility.*;
import static org.springframework.http.HttpStatus.OK;

/**
 * Service implementation for managing notifications.
 * Provides functionality to add, update, retrieve, and send notifications,
 * including live notifications pushed to connected clients using WebSocket.
 */
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;
    private final String NOTIFICATION_WITH_ID = "Notification with id:";
    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);


    /**
     * Adds a notification and checks if there is already a notification for the same book this month.
     *
     * @param request The request containing notification details.
     * @throws NotificationMessageInvalidException if the notification message is invalid.
     * @throws RuntimeException                    if there is already a notification for the book this month.
     */
    @Override
    public void addNotification(AddNotificationRequest request) {
        if (request.message() == null) {
            throw new NotificationMessageInvalidException("Notification message is " + INVALID_INFORMATION);
        }
        if (existsByBookId(request.bookId())) {
            throw new NotificationBookException("There is a notification for book with id: " + request.bookId());
        }
        var notification = createNotification(request);
        repository.save(notification);
    }

    /**
     * Updates the status of a notification to "read" based on its ID.
     *
     * @param id The ID of the notification to be updated.
     * @return The response indicating the update status.
     * @throws NotificationNotFoundException if the notification with the given ID is not found.
     */
    @Override
    public UpdateNotificationResponse updateNotification(Long id) {
        var notification = repository.findById(id)
                .orElseThrow(() -> new NotificationNotFoundException(ELibraryUtility.stringFormatter(NOTIFICATION_WITH_ID, id, NOT_FOUND)));
        notification.setIsRead(true);
        repository.save(notification);
        return new UpdateNotificationResponse(OK.value(), stringFormatter(NOTIFICATION_WITH_ID, id, UPDATED_SUCCESSFULLY));
    }



    /**
     * Retrieves a paginated list of notifications based on the specified criteria in the request.
     *
     * @param request The request containing pagination and filtering details.
     * @return A {@link NotificationPaginationResponse} containing the paginated list of notifications.
     * @throws NotificationSizeException If the specified page size is invalid.
     * @throws NotificationPageException If the specified page number is invalid.
     * @throws NotificationTypeException If the specified notification type is invalid.
     */
    @Override
    public NotificationPaginationResponse getNotifications(NotificationPaginationRequest request) {
        Page<Notification> data;
        if (!checkPageSize(request.size())) {
            throw new NotificationSizeException(stringFormatter("Size:", request.size(), IS_INVALID));
        }
        if (request.page() < 0) {
            throw new NotificationPageException(stringFormatter("Page:", request.page(), IS_INVALID));
        }
        if (!isNotificationTypeAccurate(request)) {
            throw new NotificationTypeException(stringFormatter("Notification type: ", request.notificationType(), "is wrong type"));
        }
        Pageable pageable = PageRequest.of(request.page(), request.size());
        if (request.notificationType().equals(ALL)) {
            data = repository.findAll(pageable);
        } else if (request.notificationType().equals(READ)) {
            data = repository.findByIsRead(true, pageable);
        } else {
            data = repository.findByIsRead(false, pageable);
        }
        return new NotificationPaginationResponse(OK.value(), SUCCESS, data.getContent());
    }

    /**
     * Creates a new notification object based on the provided request.
     *
     * @param request The request containing notification details.
     * @return The newly created notification.
     */
    private Notification createNotification(AddNotificationRequest request) {
        return Notification
                .builder()
                .bookId(request.bookId())
                .message(request.message())
                .isRead(false)
                .addedOn(LocalDate.now())
                .build();
    }


    /**
     * Checks if the provided notification type in the request is accurate.
     *
     * @param request The request containing notification type details.
     * @return {@code true} if the notification type is accurate, {@code false} otherwise.
     */
    private Boolean isNotificationTypeAccurate(NotificationPaginationRequest request) {
        var list = Arrays.asList(ALL, UN_READ, READ);
        return list.contains(request.notificationType());
    }

    private Boolean existsByBookId(Long bookId) {
        BigInteger result = repository.existsByBookId(bookId);
        return result != null && result.intValue() > 0;
    }
}
