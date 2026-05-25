package com.example.identityservice.service;

import com.example.identityservice.dto.request.RoleCreateRequest;
import com.example.identityservice.dto.response.RoleResponse;
import com.example.identityservice.entity.Permission;
import com.example.identityservice.entity.Role;
import com.example.identityservice.enums.TypeOfPermission;
import com.example.identityservice.mapper.RoleMapper;
import com.example.identityservice.repository.PermissionRepository;
import com.example.identityservice.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class RoleService {
    private static final Logger log = LoggerFactory.getLogger(RoleService.class);
    RoleRepository roleRepository;
    RoleMapper roleMapper;
   PermissionRepository permissionRepository;

    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }
    public RoleResponse createRole(RoleCreateRequest request){
        Role role = roleMapper.toRole(request);
        Set<Permission> permissions = new HashSet<>();
        request.getPermissions().forEach(permission -> {
            permissions.add(permissionRepository.findByName(TypeOfPermission.valueOf(permission)));

        });
        role.setPermissions(permissions);
       return roleMapper.toRoleResponse(roleRepository.save(role));
    }
}
