package com.traveler.useractivity.domain.rule.format.repository;

import com.traveler.useractivity.domain.rule.format.entity.FormatRule;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FormatRuleRepository extends JpaRepository<FormatRule, Long> {
    Page<FormatRule> findByLogProcessId(Long logProcessId, Pageable pageable);

    @Cacheable(cacheNames = "activeFormatRules", key = "#logProcessId")
    List<FormatRule> findAllByLogProcessIdAndIsActiveTrue(Long logProcessId);
}
