package com.example.identityservice.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public enum TypeOfPermission {
    READ_DATA("READ_DATA"),
    WRITE_DATA("WRITE_DATA"),
    DELETE_DATA("DELETE_DATA"),
    UPDATE_DATA("UPDATE_DATA");
    String permissionName;
}
