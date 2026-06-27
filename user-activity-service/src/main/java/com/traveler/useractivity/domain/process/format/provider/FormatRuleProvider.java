package com.traveler.useractivity.domain.process.format.provider;

import com.traveler.useractivity.domain.process.format.model.ActiveFormatRule;
import com.traveler.useractivity.domain.rule.format.repository.FormatRuleRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FormatRuleProvider {
    private final FormatRuleRepository formatRuleRepository;

    @Cacheable(cacheNames = "activeFormatRulesCache", key = "#logProcessId")
    public List<ActiveFormatRule> getActiveFormatRules(Long logProcessId) {
        return formatRuleRepository.findAllByLogProcessIdAndIsActiveTrue(logProcessId).stream()
                .map(rule -> ActiveFormatRule.of(rule.getId(), rule.getDefaultValues(), rule.getFieldMappings()))
                .toList();
    }
}
