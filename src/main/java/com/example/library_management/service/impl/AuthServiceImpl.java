package com.example.library_management.service.impl;

import com.example.library_management.auth.JwtUtil;
import com.example.library_management.dto.LoginRequest;
import com.example.library_management.dto.LoginResponse;
import com.example.library_management.dto.RegisterResponse;
import com.example.library_management.dto.UserRegisterRequest;
import com.example.library_management.entity.Role;
import com.example.library_management.entity.User;
import com.example.library_management.exception.AuthenticationException;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.repo.RoleRepo;
import com.example.library_management.repo.UserRepo;
import com.example.library_management.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UserRepo userRepository;
    private final RoleRepo roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        try {
            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                throw new BadCredentialsException("Invalid password");
            }

            String token = jwtUtil.createToken(user);
            return new LoginResponse(token, user.getUsername(), user.getEmail());

        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Invalid email/password");
        }
    }

    @Transactional
    @Override
    public RegisterResponse register(UserRegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());

        Role role = roleRepository.findByName("USER")
                .orElseThrow(() -> new ResourceNotFoundException("Default role not found"));
        user.setRole(role);

        User savedUser = userRepository.save(user);

        RegisterResponse response = new RegisterResponse();
        response.setUsername(savedUser.getUsername());
        response.setEmail(savedUser.getEmail());
        response.setRoleId(savedUser.getRole().getId());

        return response;
    }
}
