package com.example.identityservice.service;

import com.example.identityservice.dto.request.AccountCreateRequest;
import com.example.identityservice.dto.request.UserCreateRequest;
import com.example.identityservice.dto.request.UserUpdateRequest;
import com.example.identityservice.dto.response.UserResponse;
import com.example.identityservice.entity.User;
import com.example.identityservice.enums.ErrorCode;
import com.example.identityservice.exception.AppException;
import com.example.identityservice.mapper.UserMapper;
import com.example.identityservice.repository.AccountRepository;
import com.example.identityservice.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    KafkaTemplate<String, String> kafkaTemplate;
    AccountService accountService;
    AccountRepository accountRepository;

    @PreAuthorize("hasAnyRole('ADMIN','MANAGER','STAFF')")
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    public UserResponse getUserByFirebaseId(String firebaseId) {
        User user = userRepository.findByFirebaseId(firebaseId)
                .or(() -> userRepository.findById(firebaseId))
                .or(() -> accountRepository.findById(firebaseId).map(account -> account.getUser()))
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    public UserResponse getUserById(String userID) {
        User user = userRepository.findById(userID).orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    public UserResponse createUser(UserCreateRequest request) {
        User user = userMapper.toUser(request);
        userRepository.save(user);
        accountService.createAccount(AccountCreateRequest.builder()
                        .username(user.getEmail())
                        .password(request.getPassword())
                        .roles(List.of(request.getTypeOfUser()))
                        .user(user.getId())
                .build());

        return userMapper.toUserResponse(userRepository.save(user));

    }

    public void deleteUser(String userID) {
        userRepository.deleteById(userID);
    }
    public UserResponse updateUser(String userID, UserUpdateRequest request) {
        User user = userRepository.findById(userID).orElseThrow(()-> new AppException(ErrorCode.NOT_FOUND));
        userMapper.updateUser(user, request);
        return userMapper.toUserResponse(userRepository.save(user));
    }
}
