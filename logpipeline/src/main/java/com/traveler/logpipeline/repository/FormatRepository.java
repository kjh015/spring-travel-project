package com.traveler.logpipeline.repository;

import com.traveler.logpipeline.entity.Format;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormatRepository extends JpaRepository<Format, Long> {
    List<Format> findAllByProcess_Id(Long processId);

    List<Format> findAllByIsActiveTrueAndProcess_Id(Long processId);
}
