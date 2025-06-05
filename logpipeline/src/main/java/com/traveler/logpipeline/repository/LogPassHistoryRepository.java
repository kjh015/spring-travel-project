package com.traveler.logpipeline.repository;

import com.traveler.logpipeline.entity.LogPassHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogPassHistoryRepository extends JpaRepository<LogPassHistory, Long> {
    List<LogPassHistory> findAllByDeduplication_idAndUserId(Long ddpId, String userId);
}
