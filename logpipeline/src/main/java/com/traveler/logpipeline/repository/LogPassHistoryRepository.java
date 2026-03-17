package com.traveler.logpipeline.repository;

import com.traveler.logpipeline.entity.LogPassHistory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogPassHistoryRepository extends JpaRepository<LogPassHistory, Long> {
    List<LogPassHistory> findAllByDeduplication_idAndUserId(Long ddpId, String userId);
}
