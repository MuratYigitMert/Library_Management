package com.example.library_management.service.impl;

import com.example.library_management.dto.BookRequest;
import com.example.library_management.entity.*;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.repo.*;
import com.example.library_management.service.IBookService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements IBookService {

    private final BookRepo bookRepo;
    private final AuthorRepo authorRepo;
    private final GenreRepo genreRepo;
    private final PublisherRepo publisherRepo;

    @Override
    public Book findById(int id) {
        return bookRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
    }

    @Override
    public Page<Book> findAll(Pageable pageable) {
        return bookRepo.findAll(pageable);
    }

    @Transactional
    @Override
    public Book addBook(BookRequest request) {
        Book book = new Book();
        book.setName(request.getName());
        book.setAuthors(authorRepo.findAllById(request.getAuthorIds()));
        book.setGenres(genreRepo.findAllById(request.getGenreIds()));
        book.setPublishers(publisherRepo.findAllById(request.getPublisherIds()));
        return bookRepo.save(book);
    }

    @Transactional
    @Override
    public Book updateBook(int id, BookRequest request) {
        Book existing = bookRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found"));

        existing.setName(request.getName());
        existing.setAuthors(authorRepo.findAllById(request.getAuthorIds()));
        existing.setGenres(genreRepo.findAllById(request.getGenreIds()));
        existing.setPublishers(publisherRepo.findAllById(request.getPublisherIds()));

        return bookRepo.save(existing);
    }

    @Transactional
    @Override
    public void deleteBook(int id) {
        if (!bookRepo.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        bookRepo.deleteById(id);
    }
    @Override
    public Page<Book> filterBooks(String name, String author, String genre, String publisher, Pageable pageable) {
        // Get all books (paged)
        Page<Book> books = bookRepo.findAll(pageable);

        // Filter in Java
        List<Book> filtered = books.getContent().stream()
                .filter(book -> isNullOrContainsIgnoreCase(book.getName(), name))
                .filter(book -> isNullOrAnyMatch(book.getAuthors(), author))
                .filter(book -> isNullOrAnyMatch(book.getGenres(), genre))
                .filter(book -> isNullOrAnyMatch(book.getPublishers(), publisher))
                .toList();

        return new PageImpl<>(filtered, pageable, filtered.size());
    }

    private boolean isNullOrContainsIgnoreCase(String field, String keyword) {
        return keyword == null || (field != null && field.toLowerCase().contains(keyword.toLowerCase()));
    }

    private <T> boolean isNullOrAnyMatch(List<T> list, String keyword) {
        if (keyword == null) return true;

        return list.stream().anyMatch(item -> {
            if (item instanceof Author a) {
                return a.getName().toLowerCase().contains(keyword.toLowerCase());
            } else if (item instanceof Genre g) {
                return g.getName().toLowerCase().contains(keyword.toLowerCase());
            } else if (item instanceof Publisher p) {
                return p.getName().toLowerCase().contains(keyword.toLowerCase());
            }
            return false; // fallback
        });
    }






}
