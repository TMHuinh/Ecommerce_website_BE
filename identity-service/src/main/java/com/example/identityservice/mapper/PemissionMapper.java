package com.example.identityservice.mapper;

import com.example.identityservice.dto.request.PermissionCreateRequest;
import com.example.identityservice.dto.response.PermissionResponse;
import com.example.identityservice.entity.Permission;
import com.example.identityservice.enums.TypeOfPermission;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PemissionMapper {
    @Mapping(target = "name",source = "name",qualifiedByName = "stringToEnum")
    Permission toPermission(PermissionCreateRequest request);
    @Mapping(target = "name",source = "name",qualifiedByName = "enumToString")
    PermissionResponse toPermissionResponse(Permission permission);
    // Chuyển từ enum sang String
    @org.mapstruct.Named("enumToString")
    default String enumToString(TypeOfPermission permission) {
        return permission != null ? permission.getPermissionName() : null;
    }
    @org.mapstruct.Named("stringToEnum")
    default TypeOfPermission stringToEnum(String permissionName) {
        for (TypeOfPermission permission : TypeOfPermission.values()) {
            if (permission.getPermissionName().equals(permissionName)) {
                return permission;
            }
        }
        return null; // Hoặc throw exception nếu không tìm thấy
    }
}
