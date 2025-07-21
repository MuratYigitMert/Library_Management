package com.example.library_management.service.impl;

import com.example.library_management.dto.DtoConverter;
import com.example.library_management.dto.PublisherRequest;
import com.example.library_management.dto.PublisherResponse;
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

    private Publisher findEntityById(int id) {
        return publisherRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher not found with id: " + id));
    }

    @Override
    public Page<PublisherResponse> findAll(Pageable pageable) {
        return publisherRepo.findAll(pageable).map(DtoConverter::toDto);
    }

    @Override
    public PublisherResponse getById(int id) {
        return DtoConverter.toDto(findEntityById(id));
    }

    @Transactional
    @Override
    public PublisherResponse addPublisher(PublisherRequest request) {
        Publisher publisher = new Publisher();
        publisher.setName(request.getName());
        return DtoConverter.toDto(publisherRepo.save(publisher));
    }

    @Transactional
    @Override
    public PublisherResponse updatePublisher(int id, PublisherRequest request) {
        Publisher publisher = findEntityById(id);
        publisher.setName(request.getName());
        return DtoConverter.toDto(publisherRepo.save(publisher));
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
