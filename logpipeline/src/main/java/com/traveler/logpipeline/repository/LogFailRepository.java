package com.traveler.logpipeline.repository;

import com.traveler.logpipeline.entity.LogFail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogFailRepository extends JpaRepository<LogFail, Long> {
    List<LogFail> findAllByProcess_Id(Long processId);
    List<LogFail> findAllByFilter_Id(Long filterId);
}
