package com.example.library_management.dto;

import lombok.Data;

@Data
public class RegisterResponse {
    private String username;
    private String email;
    private int roleId;
}
