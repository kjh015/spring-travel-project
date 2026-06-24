package com.traveler.useractivity.domain.process.filter.service;

import com.traveler.useractivity.domain.process.filter.engine.SpelExpressionEvaluator;
import com.traveler.useractivity.domain.rule.filter.entity.FilterRule;
import com.traveler.useractivity.domain.rule.filter.repository.FilterRuleRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilterService {

    private final FilterRuleRepository filterRuleRepository;

    /**
     * 로그를 평가하여, 통과하지 못한 첫 번째 필터 규칙(FilterRule)을 반환합니다.
     * 모든 규칙을 무사히 통과했다면 Optional.empty()를 반환합니다.
     */
    public Optional<FilterRule> findFirstFailedRule(Map<String, String> formattedLog, Long logProcessId) {

        List<FilterRule> activeFilters = filterRuleRepository.findAllByLogProcessIdAndIsActiveTrue(logProcessId);

        if (activeFilters.isEmpty()) {
            return Optional.empty(); // 적용할 필터가 없으면 모두 통과한 것으로 간주
        }

        // activeFilters 중 로그와 매치되지 않는(실패한) 첫 번째 룰을 찾아 반환
        return activeFilters.stream()
                .filter(rule -> !evaluateLog(rule, formattedLog))
                .findFirst();
    }

    // =========================================================================
    // 💡 Private Helper Methods
    // =========================================================================

    /**
     * 필터 규칙(FilterRule)을 기준으로 로그를 평가합니다.
     */
    private boolean evaluateLog(FilterRule rule, Map<String, String> formattedLog) {
        String expression = rule.getExpression();

        // SpEL 엔진으로 조건식 평가
        boolean isPassed = SpelExpressionEvaluator.evaluate(expression, formattedLog);

        if (!isPassed) {
            log.debug("필터 조건 불일치로 로그가 Drop 되었습니다. (필터명: {}, 조건식: {})", rule.getName(), expression);
        }

        return isPassed;
    }
}
