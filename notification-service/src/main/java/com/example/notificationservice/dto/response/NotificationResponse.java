package com.example.notificationservice.dto.response;

import com.example.notificationservice.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationResponse {
    String id;
    @JsonProperty("isRead")
    boolean isRead;
    LocalDateTime timeStamp;
    String content;
    NotificationType type;
    String accountID;
}
