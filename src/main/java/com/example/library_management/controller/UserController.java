package com.example.library_management.controller;

import com.example.library_management.dto.UserRequest;
import com.example.library_management.dto.UserResponse;
import com.example.library_management.dto.UserRoleUpdateRequest;
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
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable int id) {
        return ResponseEntity.ok(userService.getById(id));
    }

    @PostMapping
    public ResponseEntity<UserResponse> addUser(@RequestBody UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable int id, @RequestBody UserRequest request) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
