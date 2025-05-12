package com.traveler.logpipeline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.traveler.logpipeline.entity.Process;

public interface ProcessRepository extends JpaRepository<Process, Long> {
}
