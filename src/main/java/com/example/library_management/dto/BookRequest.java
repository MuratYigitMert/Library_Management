package com.example.library_management.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookRequest {
    private String name;
    private List<Integer> authorIds;
    private List<Integer> genreIds;
    private List<Integer> publisherIds;
}
