package com.example.identityservice.entity;

import com.example.identityservice.enums.TypeOfRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    @Enumerated(EnumType.STRING)
    TypeOfRole typeOfRole;
    String description;
    @ManyToMany
    Set<Permission> permissions;
}
