package com.example.notificationservice.entity;

import com.example.notificationservice.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.Timestamp;
import java.time.LocalDateTime;

@Document(collection = "Notification")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification {
    @Id
    String id;
    @JsonProperty("isRead")
    boolean isRead;
    LocalDateTime timeStamp;
    String content;
    NotificationType type;
    String accountID;
}
