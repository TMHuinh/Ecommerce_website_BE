package com.example.identityservice.entity;

import com.example.identityservice.enums.TypeOfUser;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    int age;
    String address;
    @Enumerated(EnumType.STRING)
    TypeOfUser typeOfUser;
    String phoneNumber;
    String email;
    boolean gender;
}
