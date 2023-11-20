package com.Backend.notification.repository;

import com.Backend.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {


    @Query(value = "SELECT * FROM notification"
            + " WHERE is_checked :=b"
            , nativeQuery = true)
    List<Notification> findNotification(@Param("b") Boolean b);
}
