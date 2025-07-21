package com.example.library_management.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LoanResponse {
    private int id;

    private int bookId;
    private String bookName;

    private int userId;
    private String username;

    private LocalDateTime loanDate;
    private LocalDateTime returnDate;

    private boolean returned;
}
