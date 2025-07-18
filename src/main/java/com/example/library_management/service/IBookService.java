package com.example.library_management.service;

import com.example.library_management.dto.BookRequest;
import com.example.library_management.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface IBookService {
    Book findById(int id);
    Page<Book> findAll(Pageable pageable);
    Book addBook(BookRequest request);
    Book updateBook(int id, BookRequest request);
    void deleteBook(int id);

    Page<Book> filterBooks(String name, String author, String genre, String publisher, Pageable pageable);
}
