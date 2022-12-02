package com.example.demo.service.notification;

import com.example.demo.entity.notification.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface NotificationService {
    Page<Notification> findPaginatedNotifications(Pageable pageable);

    Optional<Notification> findById(int id);

    List<Notification> findAllActiveNotifications(String userName);

    void readNotification(int id);

    void readAllNotifications();

    void deleteNotification(int id);

    void deleteAllNotifications();
}
