package com.example.voucherservice.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {

    VOUCHER_NOT_FOUND(1001, "Voucher not found", HttpStatus.BAD_REQUEST),
    VOUCHER_OUT_OF_STOCK(1002, "Voucher out of stock", HttpStatus.BAD_REQUEST),
    FORBIDDEN(1006, "Access Is Not Allowed", HttpStatus.FORBIDDEN),
    VOUCHER_CONFLICT(1003, "Voucher has been used by another request", HttpStatus.BAD_REQUEST);


    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
