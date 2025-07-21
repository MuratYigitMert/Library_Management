package com.example.library_management.service.impl;

import com.example.library_management.dto.DtoConverter;
import com.example.library_management.dto.GenreRequest;
import com.example.library_management.dto.GenreResponse;
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

    private Genre findEntityById(int id) {
        return genreRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found with id: " + id));
    }

    @Override
    public Page<GenreResponse> findAll(Pageable pageable) {
        return genreRepo.findAll(pageable).map(DtoConverter::toDto);
    }

    @Override
    public GenreResponse getById(int id) {
        return DtoConverter.toDto(findEntityById(id));
    }

    @Transactional
    @Override
    public GenreResponse addGenre(GenreRequest request) {
        Genre genre = new Genre();
        genre.setName(request.getName());
        return DtoConverter.toDto(genreRepo.save(genre));
    }

    @Transactional
    @Override
    public GenreResponse updateGenre(int id, GenreRequest request) {
        Genre genre = findEntityById(id);
        genre.setName(request.getName());
        return DtoConverter.toDto(genreRepo.save(genre));
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
