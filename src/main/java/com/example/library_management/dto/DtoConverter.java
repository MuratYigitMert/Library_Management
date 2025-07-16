package com.example.library_management.dto;

import  com.example.library_management.entity.Role;
import  com.example.library_management.entity.User;

public class DtoConverter {
    private DtoConverter() {
    }


    public static UserResponse toDto(User user) {
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setEmail(user.getEmail());
        userResponse.setRoleId(user.getRole().getId());
        userResponse.setUsername(user.getUsername());
        return userResponse;
    }


    public static RoleResponse toDto(Role role) {
        RoleResponse roleResponse = new RoleResponse();
        roleResponse.setId(role.getId());
        roleResponse.setName(role.getName());
        return roleResponse;
    }

    public static User toEntity(UserRequest request, Role role) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEmail(request.getEmail());
        user.setRole(role);
        return user;
    }

}

