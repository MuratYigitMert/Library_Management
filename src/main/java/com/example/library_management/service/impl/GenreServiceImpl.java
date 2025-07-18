package com.example.library_management.service.impl;

import com.example.library_management.dto.GenreRequest;
import com.example.library_management.entity.Genre;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.repo.GenreRepo;
import com.example.library_management.service.IGenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements IGenreService {

    private final GenreRepo genreRepo;

    @Override
    public Page<Genre> findAll(Pageable pageable) {
        return genreRepo.findAll(pageable);
    }

    @Override
    public Genre findById(int id) {
        return genreRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + id));
    }

    @Transactional
    @Override
    public Genre addGenre(GenreRequest request) {
        Genre genre = new Genre();
        genre.setName(request.getName());
        return genreRepo.save(genre);
    }

    @Transactional
    @Override
    public Genre updateGenre(int id, GenreRequest request) {
        Genre existing = findById(id);
        existing.setName(request.getName());
        return genreRepo.save(existing);
    }

    @Transactional
    @Override
    public void deleteGenre(int id) {
        if (!genreRepo.existsById(id)) {
            throw new ResourceNotFoundException("Genre not found with id: " + id);
        }
        genreRepo.deleteById(id);
    }
}
