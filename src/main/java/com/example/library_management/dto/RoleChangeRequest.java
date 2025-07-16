package com.example.library_management.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RoleChangeRequest {
    private String role;
    private int id;
}
