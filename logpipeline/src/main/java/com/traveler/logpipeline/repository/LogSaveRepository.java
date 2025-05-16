package com.traveler.logpipeline.repository;

import com.traveler.logpipeline.entity.LogSave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogSaveRepository extends JpaRepository<LogSave, Long> {
}
