package com.example.identityservice.dto.request;

import com.example.identityservice.enums.TypeOfPermission;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionCreateRequest {
    String name;
    String description;
}
