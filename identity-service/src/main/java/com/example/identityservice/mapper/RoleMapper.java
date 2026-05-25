package com.example.identityservice.mapper;

import com.example.identityservice.dto.request.RoleCreateRequest;
import com.example.identityservice.dto.response.RoleResponse;
import com.example.identityservice.entity.Role;
import com.example.identityservice.enums.TypeOfPermission;
import com.example.identityservice.enums.TypeOfRole;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel ="spring")
public interface RoleMapper {
    @Mapping(target ="typeOfRole",source = "typeOfRole",qualifiedByName = "stringToEnum")
    @Mapping(target = "permissions",ignore = true)
    Role toRole(RoleCreateRequest request);
    @Mapping(target ="typeOfRole",source = "typeOfRole",qualifiedByName = "enumToString")
    RoleResponse toRoleResponse(Role role);



    // Chuyển từ enum sang String
    @org.mapstruct.Named("enumToString")
    default String enumToString(TypeOfRole role) {
        return role != null ? role.getRoleName() : null;
    }
    @org.mapstruct.Named("stringToEnum")
    default TypeOfRole stringToEnum(String roleName) {
        for (TypeOfRole role : TypeOfRole.values()) {
            if (role.getRoleName().equals(roleName)) {
                return role;
            }
        }
        return null; // Hoặc throw exception nếu không tìm thấy
    }
}
