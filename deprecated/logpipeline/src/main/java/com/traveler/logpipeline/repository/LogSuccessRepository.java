package com.traveler.logpipeline.repository;

import com.traveler.logpipeline.entity.LogSuccess;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogSuccessRepository extends JpaRepository<LogSuccess, Long> {
    List<LogSuccess> findAllByProcess_Id(Long processId);
}
