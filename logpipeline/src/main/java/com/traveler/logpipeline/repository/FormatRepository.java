package com.traveler.logpipeline.repository;

import com.traveler.logpipeline.entity.Format;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormatRepository extends JpaRepository<Format, Long> {
    List<Format> findAllByProcess_Id(Long processId);
}
