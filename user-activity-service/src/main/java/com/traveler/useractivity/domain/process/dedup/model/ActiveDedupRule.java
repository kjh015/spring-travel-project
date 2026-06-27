package com.traveler.useractivity.domain.process.dedup.model;

import com.traveler.useractivity.domain.rule.dedup.vo.DedupSpec;
import java.util.List;

public record ActiveDedupRule(Long dedupRuleId, String name, List<DedupSpec.Rule> specRules) {
    public static ActiveDedupRule of(Long dedupRuleId, String name, List<DedupSpec.Rule> specRules) {
        return new ActiveDedupRule(dedupRuleId, name, specRules);
    }
}
