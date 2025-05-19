package com.traveler.logpipeline.repository;

import com.traveler.logpipeline.entity.LogSuccess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogSuccessRepository extends JpaRepository<LogSuccess, Long> {
    List<LogSuccess> findAllByProcess_Id(Long processId);
}
