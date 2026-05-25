package com.example.identityservice.dto.request;

import com.example.identityservice.enums.TypeOfUser;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String name;
    int age;
    String address;
    String typeOfUser;
    String phoneNumber;
    String email;
    boolean gender;
}
