package com.example.library_management.controller;

import com.example.library_management.dto.BookRequest;
import com.example.library_management.dto.BookResponse;
import com.example.library_management.dto.DtoConverter;
import com.example.library_management.entity.Book;
import com.example.library_management.service.IBookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final IBookService bookService;

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable int id) {
        Book book = bookService.findById(id);
        return ResponseEntity.ok(DtoConverter.toDto(book));
    }

    @GetMapping
    public ResponseEntity<Page<BookResponse>> getAllBooks(Pageable pageable) {
        Page<Book> books = bookService.findAll(pageable);
        Page<BookResponse> response = books.map(DtoConverter::toDto);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BookResponse> addBook(@RequestBody BookRequest request) {
        Book created = bookService.addBook(request);
        BookResponse response = DtoConverter.toDto(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable int id, @RequestBody BookRequest request) {
        Book updated = bookService.updateBook(id, request);
        BookResponse response = DtoConverter.toDto(updated);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable int id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<BookResponse>> filterBooks(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String publisher,
            Pageable pageable
    ) {
        Page<Book> filtered = bookService.filterBooks(name, author, genre, publisher, pageable);
        Page<BookResponse> responses = filtered.map(DtoConverter::toDto);
        return ResponseEntity.ok(responses);
    }

}
