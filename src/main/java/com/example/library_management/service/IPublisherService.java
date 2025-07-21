package com.example.library_management.service;

import com.example.library_management.dto.PublisherRequest;
import com.example.library_management.dto.PublisherResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPublisherService {
    Page<PublisherResponse> findAll(Pageable pageable);
    PublisherResponse getById(int id);
    PublisherResponse addPublisher(PublisherRequest request);
    PublisherResponse updatePublisher(int id, PublisherRequest request);
    void deletePublisher(int id);
}
