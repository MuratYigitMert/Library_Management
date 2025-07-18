package com.example.library_management.service.impl;

import com.example.library_management.dto.UserRequest;
import com.example.library_management.entity.Role;
import com.example.library_management.entity.User;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.repo.RoleRepo;
import com.example.library_management.repo.UserRepo;
import com.example.library_management.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User findById(int id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return userRepo.findAll(pageable);
    }

    @Override
    public User createUser(UserRequest request) {
        Role role = roleRepo.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(role);

        return userRepo.save(user);
    }

    @Override
    public User updateUser(int id, UserRequest request) {
        User existing = findById(id);
        Role role = roleRepo.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        existing.setUsername(request.getUsername());
        existing.setPassword(passwordEncoder.encode(request.getPassword()));
        existing.setEmail(request.getEmail());
        existing.setRole(role);

        return userRepo.save(existing);
    }

    @Override
    public void deleteUser(int id) {
        if (!userRepo.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepo.deleteById(id);
    }
    @Override
    public User updateUserRole(int userId, String roleName) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        Role role = roleRepo.findByName(roleName.toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));
        user.setRole(role);
        return userRepo.save(user);
    }
}
