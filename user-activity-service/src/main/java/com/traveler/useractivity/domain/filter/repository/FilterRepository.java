package com.traveler.useractivity.domain.filter.repository;

import com.traveler.useractivity.domain.filter.entity.Filter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilterRepository extends JpaRepository<Filter, Long> {}
