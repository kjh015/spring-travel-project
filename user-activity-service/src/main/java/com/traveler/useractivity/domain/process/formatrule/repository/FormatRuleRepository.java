package com.traveler.useractivity.domain.process.formatrule.repository;

import com.traveler.useractivity.domain.process.formatrule.entity.FormatRule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormatRuleRepository extends JpaRepository<FormatRule, Long> {
    Page<FormatRule> findByLogProcessId(Long logProcessId, Pageable pageable);
}
