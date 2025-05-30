package com.traveler.logpipeline.repository;

import com.traveler.logpipeline.entity.Deduplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeduplicationRepository extends JpaRepository<Deduplication, Long> {
    List<Deduplication> findAllByProcess_Id(Long processId);
    List<Deduplication> findAllByIsActiveTrueAndProcess_Id(Long processId);
}
