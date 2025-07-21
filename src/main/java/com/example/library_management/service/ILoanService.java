package com.example.library_management.service;

import com.example.library_management.dto.LoanRequest;
import com.example.library_management.dto.LoanResponse;
import com.example.library_management.entity.Loan;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ILoanService {

    LoanResponse addLoan(LoanRequest request);

    LoanResponse borrowBook(LoanRequest request);

    LoanResponse returnBook(int loanId);

    @Transactional
    LoanResponse updateLoan(int id, LoanRequest request);

    void deleteLoan(int id);

    Loan findById(int id);
    LoanResponse getLoanDtoById(int id);
    @Transactional
    LoanResponse markLoanReturned(int id);

    Page<LoanResponse> getAllLoans(Pageable pageable);

    Page<LoanResponse> findAll(Pageable pageable);

    List<LoanResponse> getLoanHistoryForUser(int userId);

    List<LoanResponse> getActiveLoansForUser(int userId);

    List<LoanResponse> getOverdueLoans();

    List<LoanResponse> findByUserId(int userId);

    List<LoanResponse> findActiveLoansByUser(int userId);

    List<LoanResponse> findLostBooks();
}
