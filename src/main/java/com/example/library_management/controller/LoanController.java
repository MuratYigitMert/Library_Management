package com.example.library_management.controller;

import com.example.library_management.dto.DtoConverter;
import com.example.library_management.dto.LoanRequest;
import com.example.library_management.dto.LoanResponse;
import com.example.library_management.entity.Loan;
import com.example.library_management.service.ILoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final ILoanService loanService;

    @PostMapping
    public ResponseEntity<LoanResponse> createLoan(@RequestBody LoanRequest request) {
        Loan loan = loanService.addLoan(request);
        LoanResponse response = DtoConverter.toDto(loan);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanResponse> getLoanById(@PathVariable int id) {
        Loan loan = loanService.findById(id);
        return ResponseEntity.ok(DtoConverter.toDto(loan));
    }

    @GetMapping
    public ResponseEntity<Page<LoanResponse>> getAllLoans(Pageable pageable) {
        Page<Loan> loans = loanService.findAll(pageable);
        Page<LoanResponse> responses = loans.map(DtoConverter::toDto);
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoanResponse> updateLoan(@PathVariable int id, @RequestBody LoanRequest request) {
        Loan updatedLoan = loanService.updateLoan(id, request);
        LoanResponse response = DtoConverter.toDto(updatedLoan);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable int id) {
        loanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LoanResponse>> getLoansByUser(@PathVariable int userId) {
        List<Loan> loans = loanService.findByUserId(userId);
        return ResponseEntity.ok(DtoConverter.toLoanDtoList(loans));
    }

    @GetMapping("/user/{userId}/active")
    public ResponseEntity<List<LoanResponse>> getActiveLoansByUser(@PathVariable int userId) {
        List<Loan> loans = loanService.findActiveLoansByUser(userId);
        return ResponseEntity.ok(DtoConverter.toLoanDtoList(loans));
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<LoanResponse> markLoanReturned(@PathVariable int id) {
        Loan loan = loanService.markLoanReturned(id);
        return ResponseEntity.ok(DtoConverter.toDto(loan));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/lost")
    public ResponseEntity<List<LoanResponse>> getLostBooks() {
        List<Loan> lostLoans = loanService.findLostBooks();
        return ResponseEntity.ok(DtoConverter.toLoanDtoList(lostLoans));
    }
}
