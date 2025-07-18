package com.example.library_management.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PublisherResponse {
    private int id;
    private String name;
}
