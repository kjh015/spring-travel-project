package com.traveler.useractivity.domain.process.core.message;

import com.traveler.useractivity.domain.process.core.code.ProcessErrorCode;

public record ErrorInfo(ProcessErrorCode code, Long failRuleId, String failRuleName, String detail) {}
