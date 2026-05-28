package com.example.identityservice.controller;

import com.example.identityservice.dto.request.UserCreateRequest;
import com.example.identityservice.dto.request.UserUpdateRequest;
import com.example.identityservice.dto.response.ApiResponse;
import com.example.identityservice.dto.response.UserResponse;
import com.example.identityservice.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class UserController {
    UserService userService;
    @GetMapping
    public ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getAllUsers())
                .build();
    }

    @GetMapping("/fbid/{firebaseId}")
    public ApiResponse<UserResponse> getUserByFirebaseId(@PathVariable String firebaseId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserByFirebaseId(firebaseId))
                .build();
    }

    @GetMapping("/{userID}")
    public ApiResponse<UserResponse> getUserById(@PathVariable String userID) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserById(userID))
                .build();
    }

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody UserCreateRequest request){
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }
    @PostMapping("/create")
    public ApiResponse<UserResponse> createUserViaCreate(@RequestBody UserCreateRequest request){
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }
    @DeleteMapping("/{userID}")
    public ApiResponse<UserResponse> deleteUser(@PathVariable String userID){
        userService.deleteUser(userID);
        return ApiResponse.<UserResponse>builder()
                .message("User with ID " + userID + " deleted")
                .build();
    }
    @PutMapping("/{userID}")
    public ApiResponse<UserResponse> updateUser(@PathVariable String userID, @RequestBody UserUpdateRequest request){
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userID, request))
                .build();
    }
}

