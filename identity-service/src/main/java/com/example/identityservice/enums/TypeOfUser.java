package com.example.identityservice.enums;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public enum TypeOfUser {
    CUSTOMER("CUSTOMER"),
    STAFF("STAFF"),
    MANAGER("MANAGER"),
    ADMIN("ADMIN");
    String name;

}
