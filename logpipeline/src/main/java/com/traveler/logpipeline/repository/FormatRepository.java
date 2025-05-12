package com.traveler.logpipeline.repository;

import com.traveler.logpipeline.entity.Format;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FormatRepository extends JpaRepository<Format, String> {
    List<Format> findAllByProcessId(String processId);
}
