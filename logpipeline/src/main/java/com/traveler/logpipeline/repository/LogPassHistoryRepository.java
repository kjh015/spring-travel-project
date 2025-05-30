package com.traveler.logpipeline.repository;

import com.traveler.logpipeline.entity.LogPassHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LogPassHistoryRepository extends JpaRepository<LogPassHistory, Long> {
    Optional<LogPassHistory> findByDeduplication_idAndUserId(Long ddpId, String userId);
}
