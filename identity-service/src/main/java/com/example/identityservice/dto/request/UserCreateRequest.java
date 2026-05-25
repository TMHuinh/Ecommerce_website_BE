package com.example.identityservice.dto.request;

import com.example.identityservice.enums.TypeOfUser;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreateRequest {
    String name;
    int age;
    String address;
    String typeOfUser;
    String phoneNumber;
    String email;
    String password;
    boolean gender;
}
