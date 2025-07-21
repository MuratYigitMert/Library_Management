package com.example.library_management.service;

import com.example.library_management.dto.GenreRequest;
import com.example.library_management.dto.GenreResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IGenreService {
    Page<GenreResponse> findAll(Pageable pageable);
    GenreResponse getById(int id);
    GenreResponse addGenre(GenreRequest request);
    GenreResponse updateGenre(int id, GenreRequest request);
    void deleteGenre(int id);
}
