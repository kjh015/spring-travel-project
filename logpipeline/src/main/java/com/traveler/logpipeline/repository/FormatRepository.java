package com.traveler.logpipeline.repository;

import com.traveler.logpipeline.entity.Format;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormatRepository extends JpaRepository<Format, String> {
}
