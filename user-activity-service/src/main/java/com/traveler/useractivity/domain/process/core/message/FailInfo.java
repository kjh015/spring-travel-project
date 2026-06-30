package com.traveler.useractivity.domain.process.core.message;

import com.traveler.useractivity.domain.process.core.code.ProcessFailCode;

public record FailInfo(ProcessFailCode code, Long failRuleId, String failRuleName, String detail) {}
