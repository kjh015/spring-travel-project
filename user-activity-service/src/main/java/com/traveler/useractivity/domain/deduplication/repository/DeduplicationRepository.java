package com.traveler.useractivity.domain.deduplication.repository;

import com.traveler.useractivity.domain.deduplication.entity.Deduplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeduplicationRepository extends JpaRepository<Deduplication, Long> {}
