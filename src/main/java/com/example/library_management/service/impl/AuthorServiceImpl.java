package com.example.library_management.service.impl;

import com.example.library_management.dto.AuthorRequest;
import com.example.library_management.dto.AuthorResponse;
import com.example.library_management.dto.DtoConverter;
import com.example.library_management.entity.Author;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.repo.AuthorRepo;
import com.example.library_management.service.IAuthorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements IAuthorService {

    private final AuthorRepo authorRepo;

    private Author findEntityById(int id) {
        return authorRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
    }

    @Override
    public AuthorResponse getById(int id) {
        Author author = findEntityById(id);
        return DtoConverter.toDto(author);
    }

    @Override
    public Page<AuthorResponse> findAll(Pageable pageable) {
        return authorRepo.findAll(pageable).map(DtoConverter::toDto);
    }

    @Transactional
    @Override
    public AuthorResponse addAuthor(AuthorRequest request) {
        Author author = new Author();
        author.setName(request.getName());
        Author saved = authorRepo.save(author);
        return DtoConverter.toDto(saved);
    }

    @Transactional
    @Override
    public AuthorResponse updateAuthor(int id, AuthorRequest request) {
        Author author = findEntityById(id);
        author.setName(request.getName());
        Author updated = authorRepo.save(author);
        return DtoConverter.toDto(updated);
    }

    @Transactional
    @Override
    public void deleteAuthor(int id) {
        if (!authorRepo.existsById(id)) {
            throw new ResourceNotFoundException("Author not found with id: " + id);
        }
        authorRepo.deleteById(id);
    }
}
