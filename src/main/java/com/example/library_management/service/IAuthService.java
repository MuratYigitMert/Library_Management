package com.example.library_management.service;

import com.example.library_management.dto.LoginRequest;
import com.example.library_management.dto.LoginResponse;
import com.example.library_management.dto.RegisterResponse;
import com.example.library_management.dto.UserRegisterRequest;
import org.springframework.transaction.annotation.Transactional;

public interface IAuthService  {
    LoginResponse login(LoginRequest loginRequest);

    @Transactional
    RegisterResponse register(UserRegisterRequest request);

}
