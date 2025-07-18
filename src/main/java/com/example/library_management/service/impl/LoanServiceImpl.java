package com.example.library_management.service.impl;

import com.example.library_management.dto.LoanRequest;
import com.example.library_management.entity.Book;
import com.example.library_management.entity.Loan;
import com.example.library_management.entity.User;
import com.example.library_management.exception.BadRequestException;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.repo.BookRepo;
import com.example.library_management.repo.LoanRepo;
import com.example.library_management.repo.UserRepo;
import com.example.library_management.service.ILoanService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements ILoanService {

    private final LoanRepo loanRepo;
    private final BookRepo bookRepo;
    private final UserRepo userRepo;

    private static final int MAX_ALLOWED_LOANS = 3;
    private static final int DEFAULT_LOAN_DAYS = 30;
    private static final int PENALIZED_LOAN_DAYS = 15;

    @Override
    public Loan findById(int id) {
        return loanRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found with id: " + id));
    }

    @Override
    public Page<Loan> getAllLoans(Pageable pageable) {
        return loanRepo.findAll(pageable);
    }

    @Override
    public List<Loan> getLoanHistoryForUser(int userId) {
        return loanRepo.findByUserId(userId);
    }

    @Override
    public List<Loan> getActiveLoansForUser(int userId) {
        return loanRepo.findByUserIdAndReturnedFalse(userId);
    }


    @Override
    public Loan borrowBook(LoanRequest request) {
        int userId = request.getUserId();
        int bookId = request.getBookId();
        log.info("Processing loan request for userId={} and bookId={}", userId, bookId);

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        long activeLoanCount = loanRepo.countByUserIdAndReturnedFalse(userId);
        log.info("User {} has {} active loans", userId, activeLoanCount);

        if (activeLoanCount >= MAX_ALLOWED_LOANS) {
            throw new BadRequestException("User has reached the max number of borrowed books");
        }

        List<Loan> pastLoans = loanRepo.findByUserId(userId);
        int lossCount = 0;

        for (Loan loan : pastLoans) {
            if (!loan.isReturned() && loan.getReturnDate().isBefore(LocalDateTime.now())) {
                lossCount++; // still overdue
            }
        }
        log.info("User {} has {} loss(es) detected", userId, lossCount);
        int days;
        if (lossCount == 0) {
            days = DEFAULT_LOAN_DAYS; // 30
        } else if (lossCount == 1) {
            days = PENALIZED_LOAN_DAYS; // 15
        } else {
            log.warn("User {} is blocked from borrowing due to {} losses", userId, lossCount);
            throw new BadRequestException("User is no longer allowed to borrow books due to repeated losses");
        }

        LocalDateTime loanDate = request.getLoanDate() != null ? request.getLoanDate() : LocalDateTime.now();

        Loan loan = new Loan();
        loan.setUser(user);
        loan.setBook(book);
        loan.setLoanDate(loanDate);
        loan.setReturnDate(loanDate.plusDays(days));
        loan.setReturned(false);
        log.info("Loan saved with ID {}", loanRepo.save(loan).getId());
        return loanRepo.save(loan);
    }


    @Override
    public Loan returnBook(int loanId) {
        Loan loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Loan not found"));
        if (loan.isReturned()) {
            throw new BadRequestException("This loan has already been returned.");
        }

        loan.setReturned(true);
        loan.setReturnDate(LocalDateTime.now());
        return loanRepo.save(loan);
    }

    @Override
    public List<Loan> getOverdueLoans() {
        return loanRepo.findByReturnedFalseAndReturnDateBefore(LocalDateTime.now());
    }




    @Override
    public Loan addLoan(LoanRequest request) {
        return borrowBook(request);
    }

    @Override
    @Transactional
    public Loan updateLoan(int id, LoanRequest request) {
        Loan loan = findById(id);
        loan.setLoanDate(request.getLoanDate());
        loan.setReturnDate(request.getReturnDate());
        loan.setReturned(request.isReturned());
        return loanRepo.save(loan);
    }

    @Override
    public void deleteLoan(int id) {
        if (!loanRepo.existsById(id)) {
            throw new ResourceNotFoundException("Loan not found");
        }
        loanRepo.deleteById(id);
    }

    @Override
    public List<Loan> findByUserId(int userId) {
        return getLoanHistoryForUser(userId);
    }

    @Override
    public List<Loan> findActiveLoansByUser(int userId) {
        return getActiveLoansForUser(userId);
    }
    @Transactional
    @Override
    public Loan markLoanReturned(int id) {
        return returnBook(id);
    }

    @Override
    public List<Loan> findLostBooks() {
        return getOverdueLoans();
    }

    @Override
    public Page<Loan> findAll(Pageable pageable) {
        return getAllLoans(pageable);
    }

}
