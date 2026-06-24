package com.traveler.useractivity.domain.process.dedup.service;

import com.traveler.useractivity.domain.process.dedup.repository.DedupHistoryRepository;
import com.traveler.useractivity.domain.rule.dedup.entity.DedupRule;
import com.traveler.useractivity.domain.rule.dedup.enums.MatchType;
import com.traveler.useractivity.domain.rule.dedup.repository.DedupRuleRepository;
import com.traveler.useractivity.domain.rule.dedup.vo.DedupSpec;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DedupService {

    private final DedupRuleRepository dedupRuleRepository;
    private final DedupHistoryRepository dedupHistoryRepository;

    /**
     * 로그를 평가하여, 중복으로 판정된 첫 번째 DedupRule을 반환합니다.
     */
    public Optional<DedupRule> findFirstDuplicatedRule(Map<String, String> logData, Long logProcessId) {
        List<DedupRule> activeRules = dedupRuleRepository.findAllByLogProcessIdAndIsActiveTrue(logProcessId);

        if (activeRules.isEmpty()) {
            return Optional.empty();
        }

        // 💡 1. 2중 for문 제거: "활성화된 룰 중에서, 이 로그를 중복으로 판정한 첫 번째 룰을 찾아라"
        return activeRules.stream()
                .filter(rule -> matchesDuplicateRule(rule, logData))
                .findFirst();
    }

    // =========================================================================
    // 💡 Private Helper Methods
    // =========================================================================

    /**
     * 단일 DedupRule 내의 여러 스펙(Spec) 중 하나라도 중복 조건에 걸리는지 확인합니다.
     */
    private boolean matchesDuplicateRule(DedupRule rule, Map<String, String> logData) {
        // "검사 대상(Subject)인 스펙들 중에서, 하나라도 Redis 캐시에 걸리는(anyMatch) 녀석이 있는가?"
        return rule.getRules().stream()
                .filter(specRule -> matchesConditions(specRule.conditions(), logData))
                .anyMatch(specRule -> isDuplicateEntry(rule, specRule, logData));
    }

    /**
     * 현재 로그가 해당 조건식들을 모두 만족하여 '중복 검사 대상'이 되는지 확인합니다.
     */
    private boolean matchesConditions(List<DedupSpec.Condition> conditions, Map<String, String> logData) {
        // "모든(allMatch) 조건이 충족되어야 검사 대상이다"
        return conditions.stream().allMatch(cond -> matchesCondition(cond, logData));
    }

    /**
     * 단일 조건(Condition)을 만족하는지 검사합니다.
     */
    private boolean matchesCondition(DedupSpec.Condition cond, Map<String, String> logData) {
        String logValue = logData.get(cond.field());

        if (logValue == null) {
            return false;
        }
        if (cond.matchType() == MatchType.EXACT_MATCH) {
            return logValue.equals(cond.value());
        }
        return true; // ANY_VALUE 인 경우
    }

    /**
     * Redis에 원자적 저장을 시도하고 실제 중복 여부를 반환합니다.
     */
    private boolean isDuplicateEntry(DedupRule rule, DedupSpec.Rule specRule, Map<String, String> logData) {
        String dedupKey = generateDedupKey(specRule.conditions(), logData);
        long ttlSeconds = specRule.expirationTime().toTotalSeconds();

        boolean isSaved = dedupHistoryRepository.saveIfAbsent(rule.getId(), dedupKey, ttlSeconds);

        if (!isSaved) {
            log.debug("중복 로그 발견 (규칙명: {}, 키: {})", rule.getName(), dedupKey);
            return true; // 중복됨
        }
        return false; // 중복 아님 (최초 진입으로 저장 성공)
    }

    /**
     * 조건식의 필드명과 로그의 실제 값을 조합하여 고유한 해시/키를 생성합니다.
     * 예: "action:click|user_id:user123"
     */
    private String generateDedupKey(List<DedupSpec.Condition> conditions, Map<String, String> logData) {
        return conditions.stream()
                .map(cond -> cond.field() + ":" + logData.get(cond.field()))
                .collect(Collectors.joining("|"));
    }
}
