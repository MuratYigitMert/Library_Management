package com.example.library_management.service;

import com.example.library_management.dto.LoanRequest;
import com.example.library_management.entity.Loan;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ILoanService {

    Loan findById(int id);

    Page<Loan> getAllLoans(Pageable pageable);

    List<Loan> getLoanHistoryForUser(int userId);

    List<Loan> getActiveLoansForUser(int userId);


    Loan borrowBook(LoanRequest request);

    Loan returnBook(int loanId);

    List<Loan> getOverdueLoans();

    @Transactional
    Loan addLoan(LoanRequest request);

    Loan updateLoan(int id, LoanRequest request);

    void deleteLoan(int id);

    List<Loan> findByUserId(int userId);

    List<Loan> findActiveLoansByUser(int userId);

    @Transactional
    Loan markLoanReturned(int id);

    List<Loan> findLostBooks();

    Page<Loan> findAll(Pageable pageable);
}
