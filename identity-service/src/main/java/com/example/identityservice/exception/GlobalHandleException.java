package com.example.identityservice.exception;

import com.example.identityservice.dto.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalHandleException {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse> handleAppException(AppException e) {
        return ResponseEntity.status(e.errorCode.getHttpStatus())
                .body(ApiResponse.builder()
                        .code(e.errorCode.getCode())
                        .message(e.errorCode.getMessage())
                        .build());
    }
}
