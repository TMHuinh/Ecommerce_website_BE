package com.example.identityservice.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountResponse {
    String id;
    String username;
    List<RoleResponse> roles;
    String user;
}
