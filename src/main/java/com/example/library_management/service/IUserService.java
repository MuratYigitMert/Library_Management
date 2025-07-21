package com.example.library_management.service;

import com.example.library_management.dto.UserRequest;
import com.example.library_management.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {
    void updateUserRole(int userId, String roleName);
    UserResponse getById(int id);
    Page<UserResponse> findAll(Pageable pageable);
    UserResponse createUser(UserRequest request);
    UserResponse updateUser(int id, UserRequest request);
    void deleteUser(int id);
}
