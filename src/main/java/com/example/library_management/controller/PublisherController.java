package com.example.library_management.controller;

import com.example.library_management.dto.DtoConverter;
import com.example.library_management.dto.PublisherRequest;
import com.example.library_management.dto.PublisherResponse;
import com.example.library_management.entity.Publisher;
import com.example.library_management.service.IPublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/publishers")
@RequiredArgsConstructor
public class PublisherController {

    private final IPublisherService publisherService;

    @GetMapping
    public ResponseEntity<Page<PublisherResponse>> getAllPublishers(Pageable pageable) {
        Page<Publisher> page = publisherService.findAll(pageable);
        Page<PublisherResponse> dtoPage = page.map(DtoConverter::toDto);
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PublisherResponse> getPublisherById(@PathVariable int id) {
        Publisher publisher = publisherService.findById(id);
        return ResponseEntity.ok(DtoConverter.toDto(publisher));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<PublisherResponse> addPublisher(@RequestBody PublisherRequest request) {
        Publisher created = publisherService.addPublisher(request);
        return new ResponseEntity<>(DtoConverter.toDto(created), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<PublisherResponse> updatePublisher(@PathVariable int id, @RequestBody PublisherRequest request) {
        Publisher updated = publisherService.updatePublisher(id, request);
        return ResponseEntity.ok(DtoConverter.toDto(updated));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable int id) {
        publisherService.deletePublisher(id);
        return ResponseEntity.noContent().build();
    }
}
