package com.example.identityservice.dto.request;

import com.example.identityservice.enums.TypeOfRole;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RoleCreateRequest {
    String typeOfRole;
    String description;
    List<String> permissions;
}
