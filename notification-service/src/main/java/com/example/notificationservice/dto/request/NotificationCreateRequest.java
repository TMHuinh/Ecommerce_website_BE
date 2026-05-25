package com.example.notificationservice.dto.request;

import com.example.notificationservice.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationCreateRequest {
    @JsonProperty("isRead")
    boolean isRead;
    LocalDateTime timeStamp;
    String content;
    NotificationType type;
    String accountID;
}
