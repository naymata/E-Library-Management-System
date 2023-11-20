package com.Backend.book.scheduler;

import com.Backend.book.repository.BookRepository;
import com.Backend.book.service.BookSchedulerService;
import com.Backend.notification.dto.AddNotificationRequest;
import com.Backend.notification.exceptions.NotificationMessageInvalidException;
import com.Backend.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
public class BookSchedulerServiceImpl implements BookSchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(BookSchedulerServiceImpl.class);
    private final BookRepository repository;
    private final NotificationService notificationService;

    @Override
    @Scheduled(cron = "0 */15 * * * ?")
    public void quantityScheduleChecker() {
        logger.info("Started quantity schedule checker at time: " + LocalDate.now());
        repository.findAll().forEach(b -> {
            if (b.getQuantity() < 10) {
                try {
                    var request = new AddNotificationRequest("Book with id: " + b.getId() + " has low quantity");
                    notificationService.addNotification(request);
                    logger.info("Created low quantity notification for book with id: " + b.getId() + " at time: " + LocalDate.now());
                } catch (NotificationMessageInvalidException e) {
                    throw new RuntimeException(e);
                } catch (RuntimeException e) {
                    logger.error(e.getMessage());
                }
            }
        });
        logger.info("Ended quantity schedule checker at time: " + LocalDate.now());
    }
}
