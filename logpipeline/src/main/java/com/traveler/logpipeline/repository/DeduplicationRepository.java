package com.traveler.logpipeline.repository;

import com.traveler.logpipeline.entity.Deduplication;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeduplicationRepository extends JpaRepository<Deduplication, Long> {
    List<Deduplication> findAllByProcess_Id(Long processId);

    List<Deduplication> findAllByIsActiveTrueAndProcess_Id(Long processId);
}
