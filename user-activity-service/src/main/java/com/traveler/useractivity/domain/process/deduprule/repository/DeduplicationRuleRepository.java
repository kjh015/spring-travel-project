package com.traveler.useractivity.domain.process.deduprule.repository;

import com.traveler.useractivity.domain.process.deduprule.entity.DeduplicationRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeduplicationRuleRepository extends JpaRepository<DeduplicationRule, Long> {
    Page<DeduplicationRule> findByLogProcessId(Long logProcessId, Pageable pageable);
}
