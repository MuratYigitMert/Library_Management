package com.example.library_management.repo;

import com.example.library_management.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LoanRepo extends JpaRepository<Loan, Integer> {

    List<Loan> findByUserId(int userId);

    List<Loan> findByUserIdAndReturnedFalse(int userId);

    long countByUserIdAndReturnedFalse(int userId);

    List<Loan> findByReturnedFalseAndReturnDateBefore(LocalDateTime now);
}























