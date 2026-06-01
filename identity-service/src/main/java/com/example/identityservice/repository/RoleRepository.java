package com.example.identityservice.repository;

import com.example.identityservice.entity.Role;
import com.example.identityservice.enums.TypeOfRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,String> {
    Role findByTypeOfRole(TypeOfRole typeOfRole);
}
