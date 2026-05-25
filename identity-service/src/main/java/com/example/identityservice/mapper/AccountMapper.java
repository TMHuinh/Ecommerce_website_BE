package com.example.identityservice.mapper;

import com.example.identityservice.dto.request.AccountCreateRequest;
import com.example.identityservice.dto.request.AccountUpdateRequest;
import com.example.identityservice.dto.response.AccountResponse;
import com.example.identityservice.dto.response.UserResponse;
import com.example.identityservice.entity.Account;
import com.example.identityservice.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "roles", ignore = true)
    Account toAccount(AccountCreateRequest request);
    @Mapping(target = "user",source = "user",qualifiedByName = "userToString")
    AccountResponse toAccountResponse(Account account);
    @Mapping(target = "roles",ignore = true)
    void updateAccount(AccountUpdateRequest request, @MappingTarget Account account);

    @Named("userToString")
    default String userResponseToString(User user) {
        return user != null ? user.getId() : null;
    }
}
