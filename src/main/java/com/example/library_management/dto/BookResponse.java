package com.example.library_management.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BookResponse {
    private int id;
    private String name;
    private List<String> authors;    // author names
    private List<String> genres;     // genre names
    private List<String> publishers; // publisher names
}
