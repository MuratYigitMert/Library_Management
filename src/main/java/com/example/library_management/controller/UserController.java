package com.example.library_management.controller;

import com.example.library_management.dto.DtoConverter;
import com.example.library_management.dto.UserRequest;
import com.example.library_management.dto.UserResponse;
import com.example.library_management.dto.UserRoleUpdateRequest;
import com.example.library_management.entity.User;
import com.example.library_management.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserController {

    private final IUserService userService;

    @PutMapping("/{userId}/role")
    public ResponseEntity<String> updateUserRole(
            @PathVariable int userId,
            @RequestBody UserRoleUpdateRequest request
    ) {
        userService.updateUserRole(userId, request.getRoleName());
        return ResponseEntity.ok("User role updated successfully.");
    }
    @GetMapping
    public ResponseEntity<Page<UserResponse>> getAllUsers(Pageable pageable) {
        Page<User> users = userService.findAll(pageable);
        Page<UserResponse> responses= users.map(DtoConverter::toDto);
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable int id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(DtoConverter.toDto(user));
    }

    @PostMapping
    public ResponseEntity<UserResponse> addUser(@RequestBody UserRequest request) {
        User created = userService.createUser(request);
        return new ResponseEntity<>(DtoConverter.toDto(created), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable int id, @RequestBody UserRequest request) {
        User updated = userService.updateUser(id, request);
        return ResponseEntity.ok(DtoConverter.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
