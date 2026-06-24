package com.traveler.useractivity.domain.rule.filter.repository;

import com.traveler.useractivity.domain.rule.filter.entity.FilterRule;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilterRuleRepository extends JpaRepository<FilterRule, Long> {
    Page<FilterRule> findByLogProcessId(Long logProcessId, Pageable pageable);

    @Cacheable(cacheNames = "activeFilterRules", key = "#logProcessId")
    List<FilterRule> findAllByLogProcessIdAndIsActiveTrue(Long logProcessId);
}
