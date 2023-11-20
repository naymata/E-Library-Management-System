package com.Backend.notification.implementation;

import com.Backend.book.exceptions.BookFieldException;
import com.Backend.book.exceptions.OrderByException;
import com.Backend.book.exceptions.PaginationPageException;
import com.Backend.book.exceptions.PaginationSizeException;
import com.Backend.book.model.Book;
import com.Backend.notification.dto.AddNotificationRequest;
import com.Backend.notification.dto.NotificationPaginationRequest;
import com.Backend.notification.dto.NotificationDataResponse;
import com.Backend.notification.exceptions.NotificationMessageInvalidException;
import com.Backend.notification.model.Notification;
import com.Backend.notification.repository.NotificationRepository;
import com.Backend.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Arrays;

import static com.Backend.utility.ELibraryUtility.*;
import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository repository;

    @Override
    public void addNotification(AddNotificationRequest request) {
        if (request.message() == null) {
            throw new NotificationMessageInvalidException("Notification message is invalid");
        }
        var notification = createNotification(request);
        repository.save(notification);
    }


    @Override
    public NotificationDataResponse getNotifications(NotificationPaginationRequest request) {
        Pageable pageable;
        Page<Book> data;

        return new NotificationDataResponse(OK.value(), SUCCESS, null);
    }

    private Notification createNotification(AddNotificationRequest request) {
        return Notification
                .builder()
                .message(request.message())
                .addedOn(LocalDate.now())
                .build();
    }
}
