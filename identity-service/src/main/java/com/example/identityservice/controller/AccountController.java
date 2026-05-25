package com.example.identityservice.controller;

import com.example.identityservice.dto.request.AccountCreateRequest;
import com.example.identityservice.dto.request.AccountUpdateRequest;
import com.example.identityservice.dto.response.AccountResponse;
import com.example.identityservice.dto.response.ApiResponse;
import com.example.identityservice.service.AccountService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/account")
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class AccountController {
    AccountService accountService;
    @PostMapping
    public ApiResponse<AccountResponse> createAccount(@RequestBody AccountCreateRequest request){
        return ApiResponse.<AccountResponse>builder()
                .result(accountService.createAccount(request))
                .build();
    }
    @GetMapping
    public ApiResponse<List<AccountResponse>> getAllAccounts(){
        return ApiResponse.<List<AccountResponse>>builder()
                .result(accountService.getAllAccounts())
                .build();
    }
    @PutMapping("/{accountID}")
    public ApiResponse<AccountResponse> updateAccount(@PathVariable String accountID, @RequestBody AccountUpdateRequest request){
        return ApiResponse.<AccountResponse>builder()
                .result(accountService.updateAccount(accountID, request))
                .build();
    }
    @DeleteMapping("/{accountID}")
    public ApiResponse<Void> deleteAccount(@PathVariable String accountID){
        accountService.deleteAccount(accountID);
        return ApiResponse.<Void>builder()
                .message("Account with ID: "+accountID+" deleted")
                .build();
    }
}
