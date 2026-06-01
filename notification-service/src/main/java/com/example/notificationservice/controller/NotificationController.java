package com.example.notificationservice.controller;

import com.example.notificationservice.dto.request.NotificationCreateRequest;
import com.example.notificationservice.dto.response.ApiResponse;
import com.example.notificationservice.dto.response.NotificationResponse;
import com.example.notificationservice.service.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notification")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class NotificationController {
    NotificationService notificationService;
    @PostMapping
    public ApiResponse<NotificationResponse> createNotification(@RequestBody NotificationCreateRequest request){
        return ApiResponse.<NotificationResponse>builder()
                .result(notificationService.createNotification(request))
                .build();
    }
    @GetMapping
    public ApiResponse<List<NotificationResponse>> getNotifications(){
        return ApiResponse.<List<NotificationResponse>>builder()
                .result(notificationService.getAllNotifications())
                .build();
    }

    @GetMapping("/account/{accountID}")
    public ApiResponse<List<NotificationResponse>> getNotificationsByAccountID(@PathVariable String accountID){
        return ApiResponse.<List<NotificationResponse>>builder()
                .result(notificationService.getNotificationsByAccountID(accountID))
                .build();
    }

    @PutMapping("/{notificationID}/read")
    public ApiResponse<NotificationResponse> markAsRead(@PathVariable String notificationID){
        return ApiResponse.<NotificationResponse>builder()
                .result(notificationService.markAsRead(notificationID))
                .build();
    }

    @PutMapping("/account/{accountID}/read-all")
    public ApiResponse<List<NotificationResponse>> markAllAsRead(@PathVariable String accountID){
        return ApiResponse.<List<NotificationResponse>>builder()
                .result(notificationService.markAllAsRead(accountID))
                .build();
    }

    @DeleteMapping("/remove-isread/{accountID}")
    public ApiResponse<?> removeIsRead(@PathVariable String accountID){
        notificationService.deleteNotificationIsReadByAccountID(accountID);
        return ApiResponse.builder()
                .message("Remove successful")
                .build();
    }
    @DeleteMapping("/{notificationID}")
    public ApiResponse<?> removeNotification(@PathVariable String notificationID){
        notificationService.deleteNotificationByID(notificationID);
        return ApiResponse.builder()
                .message("Remove successful")
                .build();
    }
}
