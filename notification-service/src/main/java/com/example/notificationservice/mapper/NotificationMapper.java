package com.example.notificationservice.mapper;

import com.example.notificationservice.dto.request.NotificationCreateRequest;
import com.example.notificationservice.dto.response.NotificationResponse;
import com.example.notificationservice.entity.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    Notification toNotification(NotificationCreateRequest request);
    NotificationResponse toNotificationResponse(Notification notification);
}
