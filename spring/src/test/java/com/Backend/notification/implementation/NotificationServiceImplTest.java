package com.Backend.notification.implementation;

import com.Backend.notification.dto.AddNotificationRequest;
import com.Backend.notification.dto.NotificationPaginationRequest;
import com.Backend.notification.dto.NotificationPaginationResponse;
import com.Backend.notification.exceptions.*;
import com.Backend.notification.model.Notification;
import com.Backend.notification.repository.NotificationRepository;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.Backend.notification.enums.NotificationType.*;
import static com.Backend.config.utility.ELibraryUtility.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class NotificationServiceImplTest {

    @Mock
    private NotificationRepository repository;
    @Mock
    private NotificationServiceImpl underTest;
    private static final Long id = 1L;
    private static final Notification notification = new Notification(null, null, "Hello", false, LocalDate.now());

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new NotificationServiceImpl(repository);
    }

    @Test
    @Order(1)
    @DisplayName("Should save notification")
    public void itShould_SaveNotification() {
        AddNotificationRequest request = new AddNotificationRequest(null, "Hello");
        underTest.addNotification(request);
        verify(repository, times(1)).save(notification);
    }

    @Test
    @DisplayName("Should throw NotificationMessageInvalidException when given no message")
    public void itShould_Throw_NotificationMessageInvalidException_When_Given_NoMessage() {
        AddNotificationRequest request = new AddNotificationRequest(null, null);

        assertThatThrownBy(() -> underTest.addNotification(request))
                .isInstanceOf(NotificationMessageInvalidException.class)
                .hasMessageContaining("Notification message is invalid information");
    }


    @Test
    @DisplayName("Should throw NotificationNotFoundException when given wrong id")
    public void itShould_Throw_NotificationNotFoundException_When_Given_Wrong_Id() {
        assertThatThrownBy(() -> underTest.updateNotification(id))
                .isInstanceOf(NotificationNotFoundException.class)
                .hasMessageContaining(stringFormatter("Notification with id:", id, NOT_FOUND));
    }

    @Test
    @DisplayName("Should update notification")
    public void itShould_UpdateNotification() {
        when(repository.findById(id)).thenReturn(Optional.of(notification));

        var response = underTest.updateNotification(id);
        assertEquals(stringFormatter("Notification with id:", id, UPDATED_SUCCESSFULLY), response.message());
    }

    @Test
    public void getNotifications_AllNotifications_Success() {

        var list = createNotificationList(5);
        Page<Notification> notificationPage = new PageImpl<>(list, PageRequest.of(0, 4), list.size());
        // Arrange
        NotificationPaginationRequest request = new NotificationPaginationRequest(0, 4, ALL);
        when(repository.findAll(Mockito.any(Pageable.class))).thenReturn(notificationPage);

        NotificationPaginationResponse response = underTest.getNotifications(request);

        assertEquals(SUCCESS, response.message());
    }

    @Test
    public void getNotifications_ReadNotifications_Success() {
        NotificationPaginationRequest request = new NotificationPaginationRequest(0, 4, READ);
        when(repository.findByIsRead(true, PageRequest.of(0, 4))).thenReturn(new PageImpl<>(Collections.emptyList()));

        NotificationPaginationResponse response = underTest.getNotifications(request);

        assertEquals(SUCCESS, response.message());
    }

    @Test
    public void getNotifications_UnreadNotifications_Success() {
        var list = createNotificationList(4);
        Page<Notification> notificationPage = new PageImpl<>(list, PageRequest.of(0, 4), list.size());
        NotificationPaginationRequest request = new NotificationPaginationRequest(0, 4, UN_READ);
        when(repository.findByIsRead(false, PageRequest.of(0, 4))).thenReturn(notificationPage);

        NotificationPaginationResponse response = underTest.getNotifications(request);

        assertEquals(SUCCESS, response.message());
    }

    @Test
    @DisplayName("Should throw a NotificationSizeException when  not given proper size")
    public void itShould_Throw_PaginationPageException() {
        assertThatThrownBy(() -> underTest.getNotifications(new NotificationPaginationRequest(0, -1, null)))
                .isInstanceOf(NotificationSizeException.class)
                .hasMessageContaining(stringFormatter("Size:", -1, IS_INVALID));
    }

    @Test
    @DisplayName("Should throw a NotificationSizeException when  not given proper page")
    public void itShould_Throw_NotificationPageException() {
        assertThatThrownBy(() -> underTest.getNotifications(new NotificationPaginationRequest(-1, 4, null)))
                .isInstanceOf(NotificationPageException.class)
                .hasMessageContaining(stringFormatter("Page:", -1, IS_INVALID));
    }

    @Test
    @DisplayName("Should throw NotificationTypeException when not given proper type")
    public void itShould_Throw_NotificationTypeException() {
        assertThatThrownBy(() -> underTest.getNotifications(new NotificationPaginationRequest(0, 4, TESTING)))
                .isInstanceOf(NotificationTypeException.class)
                .hasMessageContaining(stringFormatter("Notification type: ", TESTING, "is wrong type"));
    }

    private List<Notification> createNotificationList(Integer size) {
        List<Notification> list = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            list.add(new Notification((long) i, id, String.valueOf(i), null, LocalDate.now()));
        }
        return list;
    }
}