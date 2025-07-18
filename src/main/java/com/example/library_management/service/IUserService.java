package com.example.library_management.service;

import com.example.library_management.dto.UserRequest;
import com.example.library_management.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {
    User updateUserRole(int userId, String roleName);
    User findById(int id);
    Page<User> findAll(Pageable pageable);
    User createUser(UserRequest request);
    User updateUser(int id, UserRequest request);
    void deleteUser(int id);
}
