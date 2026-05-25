package com.example.reviewservice.exception;


import com.example.reviewservice.enums.ErrorCode;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(makeFinal = true)
public class AppException extends RuntimeException {
    ErrorCode errorCode;
}
