package com.Backend.notification.implementation;

import com.Backend.notification.dto.AddNotificationRequest;
import com.Backend.notification.exceptions.NotificationMessageInvalidException;
import com.Backend.notification.model.Notification;
import com.Backend.notification.repository.NotificationRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository repository;
    private NotificationServiceImpl underTest;

    private static final Notification notification = new Notification(null, "Hello",  LocalDate.now());

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new NotificationServiceImpl(repository);
    }

    @Test
    @DisplayName("Should save notification")
    public void itShould_SaveNotification() {
        AddNotificationRequest request = new AddNotificationRequest("Hello");
        underTest.addNotification(request);
        verify(repository, times(1)).save(notification);
    }

    @Test
    @DisplayName("Should throw NotificationMessageInvalidException when given no message")
    public void itShould_Throw_NotificationMessageInvalidException_When_Given_NoMessage() {
        AddNotificationRequest request = new AddNotificationRequest(null);

        assertThatThrownBy(() -> underTest.addNotification(request))
                .isInstanceOf(NotificationMessageInvalidException.class)
                .hasMessageContaining("Notification message is invalid");
    }





    private List<Notification> createNotificationList() {
        List<Notification> list = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            list.add(new Notification((long) i, String.valueOf(i), LocalDate.now()));
        }
        return list;
    }
}