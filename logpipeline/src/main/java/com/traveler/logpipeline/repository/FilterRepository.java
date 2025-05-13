package com.traveler.logpipeline.repository;

import com.traveler.logpipeline.entity.Filter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilterRepository extends JpaRepository<Filter, Long> {
}
