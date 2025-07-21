package com.example.library_management.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoanRequest {
    private int bookId;
    private int userId;
    private LocalDateTime loanDate;
    private LocalDateTime returnDate;  // Optional for update
    private boolean returned;          // Optional for update
}
