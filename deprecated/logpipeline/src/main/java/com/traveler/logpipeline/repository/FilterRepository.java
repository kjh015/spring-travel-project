package com.traveler.logpipeline.repository;

import com.traveler.logpipeline.entity.Filter;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilterRepository extends JpaRepository<Filter, Long> {
    List<Filter> findAllByProcess_Id(Long processId);

    List<Filter> findAllByIsActiveTrueAndProcess_Id(Long processId);
}
