package com.example.productservice.enums;

import lombok.AccessLevel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@Getter
@AllArgsConstructor
public enum Size {
    S("S"),
    M("M"),
    L("L"),
    X("X"),
    XL("XL"),
    XXL("XXL");
    String name;
}
