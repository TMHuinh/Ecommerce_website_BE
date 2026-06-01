package com.example.productservice.exception;

import com.example.productservice.enums.ErrorCode;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(makeFinal = true)
public class AppException extends RuntimeException {
    ErrorCode errorCode;
}
