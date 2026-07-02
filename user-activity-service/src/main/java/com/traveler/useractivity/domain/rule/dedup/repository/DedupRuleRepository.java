package com.traveler.useractivity.domain.rule.dedup.repository;

import com.traveler.useractivity.domain.rule.dedup.entity.DedupRule;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DedupRuleRepository extends JpaRepository<DedupRule, Long> {
    Page<DedupRule> findByLogProcessId(Long logProcessId, Pageable pageable);

    List<DedupRule> findAllByLogProcessIdAndIsActiveTrueOrderByIdAsc(Long logProcessId);
}
