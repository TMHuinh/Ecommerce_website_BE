package com.example.notificationservice.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public enum NotificationType {
    WARNING("WARNING"),
    ERROR("ERROR"),
    INFO("INFO");
    String name;
}
