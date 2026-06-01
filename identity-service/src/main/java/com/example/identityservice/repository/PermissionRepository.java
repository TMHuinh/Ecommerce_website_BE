package com.example.identityservice.repository;

import com.example.identityservice.entity.Permission;
import com.example.identityservice.enums.TypeOfPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission,String> {
    Permission findByName(TypeOfPermission name);
}
