package com.example.identityservice.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleResponse {
    String id;
    String typeOfRole;
    String description;
    Set<PermissionResponse> permissions;
}
