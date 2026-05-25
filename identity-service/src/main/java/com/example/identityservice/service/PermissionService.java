package com.example.identityservice.service;

import com.example.identityservice.dto.request.PermissionCreateRequest;
import com.example.identityservice.dto.response.PermissionResponse;
import com.example.identityservice.entity.Permission;
import com.example.identityservice.enums.TypeOfPermission;
import com.example.identityservice.mapper.PemissionMapper;
import com.example.identityservice.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
@RequiredArgsConstructor
public class PermissionService {
    PermissionRepository permissionRepository;
    PemissionMapper pemissionMapper;
    public PermissionResponse createPermission(PermissionCreateRequest request) {
        Permission permission = pemissionMapper.toPermission(request);
        return pemissionMapper.toPermissionResponse(permissionRepository.save(permission));
    }
    public List<PermissionResponse> getAllPermissions() {
        return permissionRepository.findAll().stream().map(pemissionMapper::toPermissionResponse).toList();
    }
}
