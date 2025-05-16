package com.traveler.logpipeline.repository;

import com.traveler.logpipeline.entity.Filter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilterRepository extends JpaRepository<Filter, Long> {
    List<Filter> findAllByProcess_Id(Long processId);
    List<Filter> findAllByIsActiveTrueAndProcess_Id(Long processId);
}
