package com.traveler.logpipeline.repository;

import com.traveler.logpipeline.entity.LogFailByDeduplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogFailByDeduplicationRepository extends JpaRepository<LogFailByDeduplication, Long> {
}
