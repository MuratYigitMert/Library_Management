package com.example.library_management.controller;

import com.example.library_management.dto.DtoConverter;
import com.example.library_management.dto.GenreRequest;
import com.example.library_management.dto.GenreResponse;
import com.example.library_management.entity.Genre;
import com.example.library_management.service.IGenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/genres")
@RequiredArgsConstructor
public class GenreController {

    private final IGenreService genreService;

    @GetMapping
    public ResponseEntity<Page<GenreResponse>> getAllGenres(Pageable pageable) {
        Page<Genre> page = genreService.findAll(pageable);
        Page<GenreResponse> dtoPage = page.map(DtoConverter::toDto);
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreResponse> getGenreById(@PathVariable int id) {
        Genre genre = genreService.findById(id);
        return ResponseEntity.ok(DtoConverter.toDto(genre));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<GenreResponse> addGenre(@RequestBody GenreRequest request) {
        Genre created = genreService.addGenre(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(DtoConverter.toDto(created));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<GenreResponse> updateGenre(@PathVariable int id, @RequestBody GenreRequest request) {
        Genre updated = genreService.updateGenre(id, request);
        return ResponseEntity.ok(DtoConverter.toDto(updated));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable int id) {
        genreService.deleteGenre(id);
        return ResponseEntity.noContent().build();
    }
}
