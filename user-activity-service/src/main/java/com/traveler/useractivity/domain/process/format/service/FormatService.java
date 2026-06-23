package com.traveler.useractivity.domain.process.format.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.traveler.useractivity.domain.process.format.engine.UrlQueryParser;
import com.traveler.useractivity.domain.process.format.message.RawLog;
import com.traveler.useractivity.domain.rule.format.entity.FormatRule;
import com.traveler.useractivity.domain.rule.format.repository.FormatRuleRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class FormatService {
    private final UrlQueryParser urlQueryParser;
    private final FormatRuleRepository formatRuleRepository;

    /**
     * 원본 로그에서 매핑의 재료가 될 데이터를 추출하고,
     * JSON 형태의 포맷 규칙(FormatRule)에 맞게 표준화된 로그 맵을 생성합니다.
     */
    public Map<String, String> formatLog(RawLog rawLog, Long logProcessId) {
        // 매핑의 재료가 될 출처(Source) 데이터 추출
        Map<String, String> source = extractSource(rawLog);

        // 활성화된 포맷 규칙 목록 조회 (⚠️ 추후 로컬 캐싱 적용 필수)
        List<FormatRule> activeRules = formatRuleRepository.findAllByLogProcessIdAndIsActiveTrue(logProcessId);
        if (activeRules.isEmpty()) {
            log.warn("활성화된 포맷 규칙(FormatRule)이 없습니다. 로그를 폐기합니다.");
            return Map.of();
        }

        // 조회된 규칙들을 순차적으로 적용하여 최종 포맷팅된 로그 생성
        Map<String, String> formattedLog = new HashMap<>();
        for (FormatRule rule : activeRules) {
            putDefaultValues(rule.getDefaultValues(), formattedLog);
            putFieldMappings(rule.getFieldMappings(), source, formattedLog);
        }

        return formattedLog;
    }

    // =========================================================================
    // 💡 Private Helper Methods
    // =========================================================================

    /**
     * RawLog에서 매핑에 사용할 원본 데이터(Source)를 추출하여 반환합니다.
     * (현재는 path의 쿼리 파라미터를 파싱하여 사용합니다)
     */
    private Map<String, String> extractSource(RawLog rawLog) {
        String path = rawLog.path() != null ? rawLog.path() : "";
        return urlQueryParser.extractQueryParams(path);
    }

    /**
     * 설정된 기본값(defaultValues)을 포맷팅된 로그에 추가합니다.
     */
    private void putDefaultValues(JsonNode defaultValues, Map<String, String> formattedLog) {
        if (defaultValues == null || !defaultValues.isObject()) {
            return;
        }
        defaultValues
                .fields()
                .forEachRemaining(entry ->
                        formattedLog.put(entry.getKey(), entry.getValue().asText()));
    }

    /**
     * 추출된 원본 데이터(Source)를 기반으로 필드 매핑(fieldMappings) 규칙을 적용합니다.
     * 유효한 값이 존재할 경우 포맷팅된 로그에 추가(덮어쓰기)합니다.
     */
    private void putFieldMappings(
            JsonNode fieldMappings, Map<String, String> source, Map<String, String> formattedLog) {
        if (fieldMappings == null || !fieldMappings.isObject()) {
            return;
        }
        fieldMappings.fields().forEachRemaining(entry -> {
            String targetKey = entry.getKey();
            String sourceKey = entry.getValue().asText();

            String value = source.get(sourceKey);

            if (value == null || value.isBlank()) {
                return;
            }

            formattedLog.put(targetKey, value);
        });
    }
}
