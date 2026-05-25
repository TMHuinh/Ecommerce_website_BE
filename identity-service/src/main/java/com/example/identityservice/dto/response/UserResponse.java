package com.example.identityservice.dto.response;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserResponse {
    String id;
    String name;
    int age;
    String address;
    String typeOfUser;
    String phoneNumber;
    String email;
    boolean gender;
}
