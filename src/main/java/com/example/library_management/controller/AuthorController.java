package com.example.library_management.controller;

import com.example.library_management.dto.AuthorRequest;
import com.example.library_management.dto.AuthorResponse;
import com.example.library_management.dto.DtoConverter;
import com.example.library_management.service.IAuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final IAuthorService authorService;

    @GetMapping
    public ResponseEntity<Page<AuthorResponse>> getAllAuthors(Pageable pageable) {
        return ResponseEntity.ok(authorService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getAuthorById(@PathVariable int id) {
        return ResponseEntity.ok(DtoConverter.toDto(authorService.findById(id)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<AuthorResponse> addAuthor(@RequestBody AuthorRequest request) {
        AuthorResponse created = DtoConverter.toDto(authorService.addAuthor(request));
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponse> updateAuthor(@PathVariable int id, @RequestBody AuthorRequest request) {
        AuthorResponse updated = DtoConverter.toDto(authorService.updateAuthor(id, request));
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable int id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}
