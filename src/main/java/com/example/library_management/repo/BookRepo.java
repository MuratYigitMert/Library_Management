package com.example.library_management.repo;

import com.example.library_management.entity.Book;
import lombok.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface BookRepo extends JpaRepository<Book, Integer> {
    @NonNull
    Page<Book> findAll(@NonNull Pageable pageable);

}
