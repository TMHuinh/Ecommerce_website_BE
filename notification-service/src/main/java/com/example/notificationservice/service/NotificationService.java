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

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class NotificationService {
    NotificationRepository notificationRepository;
    NotificationMapper notificationMapper;

    public NotificationResponse createNotification(NotificationCreateRequest request) {
        Notification notification = notificationMapper.toNotification(request);
        return notificationMapper.toNotificationResponse(notificationRepository.save(notification));
    }

    public List<NotificationResponse> getAllNotifications() {
        return notificationRepository.findAll().stream().map(notificationMapper::toNotificationResponse).toList();
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
