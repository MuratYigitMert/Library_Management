package com.example.library_management.controller;

import com.example.library_management.dto.LoanRequest;
import com.example.library_management.dto.LoanResponse;
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
        return new ResponseEntity<>(loanService.addLoan(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanResponse> getLoanDtoById(@PathVariable int id) {
        return ResponseEntity.ok(loanService.getLoanDtoById(id));
    }

    @GetMapping
    public ResponseEntity<Page<LoanResponse>> getAllLoans(Pageable pageable) {
        return ResponseEntity.ok(loanService.findAll(pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoanResponse> updateLoan(@PathVariable int id, @RequestBody LoanRequest request) {
        return ResponseEntity.ok(loanService.updateLoan(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable int id) {
        loanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LoanResponse>> getLoansByUser(@PathVariable int userId) {
        return ResponseEntity.ok(loanService.findByUserId(userId));
    }

    @GetMapping("/user/{userId}/active")
    public ResponseEntity<List<LoanResponse>> getActiveLoansByUser(@PathVariable int userId) {
        return ResponseEntity.ok(loanService.findActiveLoansByUser(userId));
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<LoanResponse> markLoanReturned(@PathVariable int id) {
        return ResponseEntity.ok(loanService.markLoanReturned(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/lost")
    public ResponseEntity<List<LoanResponse>> getLostBooks() {
        return ResponseEntity.ok(loanService.findLostBooks());
    }
}
