package com.example.identityservice.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class AccountCreateRequest {
    String username;
    String password;
    List<String> roles;
    String user;
}
