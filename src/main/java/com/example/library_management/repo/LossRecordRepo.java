package com.example.library_management.repo;

import com.example.library_management.entity.LossRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LossRecordRepo extends JpaRepository<LossRecord, Integer> {
}
