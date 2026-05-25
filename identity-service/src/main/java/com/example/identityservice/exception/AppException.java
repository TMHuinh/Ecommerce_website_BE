package com.example.identityservice.exception;

import com.example.identityservice.enums.ErrorCode;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(makeFinal = true)
public class AppException extends RuntimeException {
    ErrorCode errorCode;
}
