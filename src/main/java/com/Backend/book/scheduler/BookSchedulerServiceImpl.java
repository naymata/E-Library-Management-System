package com.Backend.book.scheduler;

import com.Backend.book.repository.BookRepository;
import com.Backend.book.service.BookSchedulerService;
import com.Backend.notification.dto.AddNotificationRequest;
import com.Backend.notification.exceptions.NotificationBookException;
import com.Backend.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;

import static com.Backend.config.utility.ELibraryUtility.*;

@Configuration
@RequiredArgsConstructor
public class BookSchedulerServiceImpl implements BookSchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(BookSchedulerServiceImpl.class);
    private final BookRepository repository;
    private final NotificationService notificationService;
    /**
     * Scheduled method to check book quantities and generate notifications at fixed intervals.
     * This method uses a cron expression to run every 15 minutes and checks for books with low quantities
     * using the repository. If books with low quantities are found, it creates notifications for each book
     * and logs the event using the {@link NotificationService} and logger.
     */
    @Override
    @Scheduled(cron = "0 */15 * * * ?")
    public void quantityScheduleChecker() {
        logger.info("Started quantity schedule checker at time: " + LocalDate.now());
        if (!repository.findBooksByQuantity().isEmpty()) {
            repository
                    .findBooksByQuantity()
                    .forEach(book -> {
                        try {
                            notificationService.addNotification(new AddNotificationRequest(book.getId(),
                                    stringFormatter("Book with id:", book.getId(), " is getting low on quantity")));
                            logger.info("Created notification for book with id: " + book.getId() + " at Time: " + LocalDate.now());
                        } catch (NotificationBookException e) {
                            logger.info(e.getMessage());
                        }
                    });
        }

        logger.info("Ended quantity schedule checker at time: " + LocalDate.now());
    }
}
