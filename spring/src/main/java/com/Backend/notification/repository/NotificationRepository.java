package com.Backend.notification.repository;

import com.Backend.notification.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.List;

public interface NotificationRepository extends PagingAndSortingRepository<Notification, Long>, JpaRepository<Notification, Long> {

    Page<Notification> findByIsRead(Boolean isRead, Pageable pageable);

    @Query(value = "SELECT * FROM notification n" +
            "WHERE n.is_read := value", nativeQuery = true)
    List<Notification> findAllByRead(@Param("value") Boolean value);


    @Query(value = "SELECT COUNT(*) > 0 FROM notification " +
            "WHERE book_id = :bookId " +
            "AND EXTRACT(YEAR_MONTH FROM added_on) = EXTRACT(YEAR_MONTH FROM CURRENT_DATE())", nativeQuery = true)
    BigInteger existsByBookId(@Param("bookId") Long bookId);
}
