package com.example.library_management.service.impl;

import com.example.library_management.dto.DtoConverter;
import com.example.library_management.dto.UserRequest;
import com.example.library_management.dto.UserResponse;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;

    private User findEntityById(int id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    public UserResponse getById(int id) {
        return DtoConverter.toDto(findEntityById(id));
    }

    @Override
    public Page<UserResponse> findAll(Pageable pageable) {
        return userRepo.findAll(pageable).map(DtoConverter::toDto);
    }

    @Transactional
    @Override
    public UserResponse createUser(UserRequest request) {
        Role role = roleRepo.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRole(role);

        return DtoConverter.toDto(userRepo.save(user));
    }

    @Transactional
    @Override
    public UserResponse updateUser(int id, UserRequest request) {
        User existing = findEntityById(id);
        Role role = roleRepo.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));

        existing.setUsername(request.getUsername());
        existing.setPassword(passwordEncoder.encode(request.getPassword()));
        existing.setEmail(request.getEmail());
        existing.setRole(role);

        return DtoConverter.toDto(userRepo.save(existing));
    }

    @Transactional
    @Override
    public void deleteUser(int id) {
        if (!userRepo.existsById(id)) {
            throw new ResourceNotFoundException("User not found");
        }
        userRepo.deleteById(id);
    }

    @Transactional
    @Override
    public void updateUserRole(int userId, String roleName) {
        User user = findEntityById(userId);
        Role role = roleRepo.findByName(roleName.toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));
        user.setRole(role);
        userRepo.save(user);
    }
}
