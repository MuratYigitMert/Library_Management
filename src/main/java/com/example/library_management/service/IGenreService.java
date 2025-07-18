package com.example.library_management.service;

import com.example.library_management.dto.GenreRequest;
import com.example.library_management.entity.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IGenreService {
    Page<Genre> findAll(Pageable pageable);
    Genre findById(int id);
    Genre addGenre(GenreRequest request);
    Genre updateGenre(int id, GenreRequest request);
    void deleteGenre(int id);
}
