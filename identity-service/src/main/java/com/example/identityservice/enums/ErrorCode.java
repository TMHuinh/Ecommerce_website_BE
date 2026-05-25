package com.example.identityservice.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public enum ErrorCode {
    NOT_FOUND(1001,"Not found",HttpStatus.NOT_FOUND),
    EXISTED(1002, "Existed", HttpStatus.BAD_REQUEST),
    NOT_EXISTED(1003, "Not Existed", HttpStatus.BAD_REQUEST),
    LOGIN_FAIL(1004, "Login Fail", HttpStatus.UNAUTHORIZED),
    INVALID_TOKEN(1005, "Token Is Invalid", HttpStatus.UNAUTHORIZED),
    FORBIDDEN(1006, "Access Is Not Allowed", HttpStatus.FORBIDDEN);
    int code;
    String message;
    HttpStatus httpStatus;
}
