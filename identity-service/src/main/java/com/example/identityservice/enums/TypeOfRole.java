package com.example.identityservice.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public enum TypeOfRole {
    ADMIN("ADMIN"),
    CUSTOMER("CUSTOMER"),
    MANAGER("MANAGER"),
    STAFF("STAFF");
    String roleName;
}
