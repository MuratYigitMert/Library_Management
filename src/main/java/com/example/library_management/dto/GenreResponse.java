package com.example.library_management.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GenreResponse {
    private int id;
    private String name;
}
