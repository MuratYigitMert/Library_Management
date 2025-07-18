package com.example.library_management.service;

import com.example.library_management.dto.AuthorRequest;
import com.example.library_management.dto.AuthorResponse;
import com.example.library_management.entity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAuthorService {
    Author findById(int id);
    Page<AuthorResponse> findAll(Pageable pageable);
    Author addAuthor(AuthorRequest request);
    Author updateAuthor(int id, AuthorRequest request);
    void deleteAuthor(int id);
}
