package com.traveler.useractivity.domain.process.dedup.model;

import com.traveler.useractivity.domain.rule.dedup.vo.DedupSpec;

public record DedupResult(ActiveDedupRule rule, DedupSpec.Rule matchedSpec) {}
