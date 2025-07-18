package com.example.library_management.service.impl;

import com.example.library_management.dto.PublisherRequest;
import com.example.library_management.entity.Publisher;
import com.example.library_management.exception.ResourceNotFoundException;
import com.example.library_management.repo.PublisherRepo;
import com.example.library_management.service.IPublisherService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PublisherServiceImpl implements IPublisherService {

    private final PublisherRepo publisherRepo;

    @Override
    public Page<Publisher> findAll(Pageable pageable) {
        return publisherRepo.findAll(pageable);
    }

    @Override
    public Publisher findById(int id) {
        return publisherRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found with id: " + id));
    }

    @Transactional
    @Override
    public Publisher addPublisher(PublisherRequest request) {
        Publisher publisher = new Publisher();
        publisher.setName(request.getName());
        return publisherRepo.save(publisher);
    }

    @Transactional
    @Override
    public Publisher updatePublisher(int id, PublisherRequest request) {
        Publisher existing = findById(id);
        existing.setName(request.getName());
        return publisherRepo.save(existing);
    }

    @Transactional
    @Override
    public void deletePublisher(int id) {
        if (!publisherRepo.existsById(id)) {
            throw new ResourceNotFoundException("Publisher not found with id: " + id);
        }
        publisherRepo.deleteById(id);
    }
}
