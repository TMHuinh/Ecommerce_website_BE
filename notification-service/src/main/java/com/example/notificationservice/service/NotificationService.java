package com.example.notificationservice.service;

import com.example.notificationservice.dto.request.NotificationCreateRequest;
import com.example.notificationservice.dto.response.NotificationResponse;
import com.example.notificationservice.entity.Notification;
import com.example.notificationservice.mapper.NotificationMapper;
import com.example.notificationservice.repository.NotificationRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class NotificationService {
    NotificationRepository notificationRepository;
    NotificationMapper notificationMapper;

    public NotificationResponse createNotification(NotificationCreateRequest request) {
        Notification notification = notificationMapper.toNotification(request);
        if (notification.getTimeStamp() == null) {
            notification.setTimeStamp(LocalDateTime.now());
        }
        return notificationMapper.toNotificationResponse(notificationRepository.save(notification));
    }

    public List<NotificationResponse> getAllNotifications() {
        return notificationRepository.findAll().stream().map(notificationMapper::toNotificationResponse).toList();
    }

    public List<NotificationResponse> getNotificationsByAccountID(String accountID) {
        return notificationRepository.findAllByAccountIDOrderByTimeStampDesc(accountID)
                .stream()
                .map(notificationMapper::toNotificationResponse)
                .toList();
    }

    public NotificationResponse markAsRead(String notificationID) {
        Notification notification = notificationRepository.findById(notificationID).orElseThrow();
        notification.setRead(true);
        return notificationMapper.toNotificationResponse(notificationRepository.save(notification));
    }

    public List<NotificationResponse> markAllAsRead(String accountID) {
        List<Notification> notifications = notificationRepository.findAllByAccountID(accountID);
        notifications.forEach(notification -> notification.setRead(true));
        return notificationRepository.saveAll(notifications)
                .stream()
                .map(notificationMapper::toNotificationResponse)
                .toList();
    }

    public void deleteNotificationIsReadByAccountID(String accountID) {
        List<Notification> notifications = notificationRepository.findAllByAccountID(accountID);
        List<Notification> notificationsToDelete = notifications.stream().filter(Notification::isRead).toList();
        notificationRepository.deleteAll(notificationsToDelete);
    }
    public void deleteNotificationByID(String id) {
        notificationRepository.deleteById(id);
    }
}
