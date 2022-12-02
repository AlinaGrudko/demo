package com.example.demo.service.notification.impl;

import com.example.demo.entity.notification.Notification;
import com.example.demo.entity.user.User;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.service.notification.NotificationService;
import com.example.demo.service.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public Page<Notification> findPaginatedNotifications(Pageable pageable) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findUserByUserName(auth.getName());

        List<Notification> notifications = user.getNotifications().stream()
                .sorted(Comparator.comparing(Notification::getDateTime).reversed())
                .collect(Collectors.toList());

        List<Notification> resultList;

        if(notifications.size() < startItem) {
            resultList = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, notifications.size());
            resultList = notifications.subList(startItem, toIndex);
        }

        return new PageImpl<>(resultList, PageRequest.of(currentPage, pageSize), notifications.size());
    }

    @Override
    public Optional<Notification> findById(int id) {
        return this.notificationRepository.findById(id);
    }

    @Override
    public List<Notification> findAllActiveNotifications(String userName) {
        User user = this.userService.findUserByUserName(userName);

        return user.getNotifications().stream()
                .filter(Notification::isActive)
                .sorted(Comparator.comparing(Notification::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public void readNotification(int id) {
        Optional<Notification> notificationOptional = this.notificationRepository.findById(id);
        notificationOptional.ifPresent(notification -> notification.setActive(false));
    }

    @Override
    public void readAllNotifications() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findUserByUserName(auth.getName());

        user.getNotifications().forEach(notification -> notification.setActive(false));
    }

    @Override
    public void deleteNotification(int id) {
        this.notificationRepository.deleteById(id);
    }

    @Override
    public void deleteAllNotifications() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = this.userService.findUserByUserName(auth.getName());

        user.getNotifications().clear();
    }
}
