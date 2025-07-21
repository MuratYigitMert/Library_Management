package com.example.library_management.service;

import com.example.library_management.dto.AuthorRequest;
import com.example.library_management.dto.AuthorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAuthorService {
    AuthorResponse getById(int id);
    Page<AuthorResponse> findAll(Pageable pageable);
    AuthorResponse addAuthor(AuthorRequest request);
    AuthorResponse updateAuthor(int id, AuthorRequest request);
    void deleteAuthor(int id);
}
