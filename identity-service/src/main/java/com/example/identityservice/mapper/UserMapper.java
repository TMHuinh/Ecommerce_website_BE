package com.example.identityservice.mapper;

import com.example.identityservice.dto.request.UserCreateRequest;
import com.example.identityservice.dto.request.UserUpdateRequest;
import com.example.identityservice.dto.response.UserResponse;
import com.example.identityservice.entity.User;
import com.example.identityservice.enums.TypeOfUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "typeOfUser",source = "typeOfUser",qualifiedByName = "stringToEnum")
    User toUser(UserCreateRequest request);
    @Mapping(target = "typeOfUser",source = "typeOfUser",qualifiedByName = "enumToString")
    UserResponse toUserResponse(User user);
    @Mapping(target = "typeOfUser",source = "typeOfUser",qualifiedByName = "stringToEnum")
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
    // Chuyển từ enum sang String
    @org.mapstruct.Named("enumToString")
    default String enumToString(TypeOfUser user) {
        return user != null ? user.getName() : null;
    }
    @org.mapstruct.Named("stringToEnum")
    default TypeOfUser stringToEnum(String typeOfUser) {
        for (TypeOfUser user : TypeOfUser.values()) {
            if (user.getName().equals(typeOfUser)) {
                return user;
            }
        }
        return null; // Hoặc throw exception nếu không tìm thấy
    }
}
