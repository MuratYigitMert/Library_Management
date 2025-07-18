package com.example.library_management.service;

import com.example.library_management.dto.PublisherRequest;
import com.example.library_management.entity.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPublisherService {
    Page<Publisher> findAll(Pageable pageable);
    Publisher findById(int id);
    Publisher addPublisher(PublisherRequest request);
    Publisher updatePublisher(int id, PublisherRequest request);
    void deletePublisher(int id);
}
