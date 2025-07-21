package com.example.library_management.service;

import com.example.library_management.dto.BookRequest;
import com.example.library_management.dto.BookResponse;
import io.jsonwebtoken.io.IOException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;


public interface IBookService {
    BookResponse getById(int id);
    Page<BookResponse> findAll(Pageable pageable);
    BookResponse addBook(BookRequest request);
    BookResponse updateBook(int id, BookRequest request);
    void deleteBook(int id);
    Page<BookResponse> filterBooks(String name, String author, String genre, String publisher, Pageable pageable);
    ByteArrayInputStream exportBooksToExcel() throws IOException;
    ResponseEntity<byte[]> getBooksExcelResponse() throws IOException;

}
