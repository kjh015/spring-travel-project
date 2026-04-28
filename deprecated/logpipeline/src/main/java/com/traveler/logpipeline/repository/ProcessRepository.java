package com.traveler.logpipeline.repository;

import com.traveler.logpipeline.entity.Process;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessRepository extends JpaRepository<Process, Long> {}
