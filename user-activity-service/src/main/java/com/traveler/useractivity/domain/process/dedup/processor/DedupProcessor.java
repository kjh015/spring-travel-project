package com.traveler.useractivity.domain.process.dedup.processor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.useractivity.domain.process.core.code.ProcessFailCode;
import com.traveler.useractivity.domain.process.core.dispatcher.ProcessDispatcher;
import com.traveler.useractivity.domain.process.core.handler.KafkaAckHandler;
import com.traveler.useractivity.domain.process.core.message.FailInfo;
import com.traveler.useractivity.domain.process.core.message.LogMetadata;
import com.traveler.useractivity.domain.process.core.message.LogPayload;
import com.traveler.useractivity.domain.process.dedup.model.ActiveDedupRule;
import com.traveler.useractivity.domain.process.dedup.model.DedupResult;
import com.traveler.useractivity.domain.process.dedup.provider.DedupRuleProvider;
import com.traveler.useractivity.domain.process.dedup.service.DedupService;
import com.traveler.useractivity.domain.rule.dedup.vo.DedupSpec;
import com.traveler.useractivity.global.kafka.KafkaTopicProperties;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DedupProcessor {
    private final ObjectMapper objectMapper;
    private final DedupRuleProvider dedupRuleProvider;
    private final DedupService dedupService;
    private final ProcessDispatcher processDispatcher;
    private final KafkaAckHandler ackHandler;
    private final KafkaTopicProperties topics;

    @KafkaListener(topics = "${app.kafka.topics.dedup-stream}", groupId = "${app.kafka.groups.dedup}")
    public void process(@Payload String payload, Acknowledgment ack) throws Exception {

        // JSON 페이로드를 LogPayload 객체로 역직렬화
        LogPayload<Map<String, String>> logPayload = objectMapper.readValue(payload, new TypeReference<>() {});

        // LogPayload에서 메타데이터 및 원본 로그 데이터 추출
        LogMetadata metadata = logPayload.metadata();
        Map<String, String> logData = logPayload.data();

        // 활성화된 중복 제거 규칙 조회 및 중복 여부 판별
        List<ActiveDedupRule> activeDedupRules = dedupRuleProvider.getActiveDedupRules(metadata.logProcessId());
        Optional<DedupResult> dedupResultOpt = dedupService.findFirstDuplicatedRule(logData, activeDedupRules);

        CompletableFuture<?> dispatchFuture;

        if (dedupResultOpt.isPresent()) {
            FailInfo failInfo = createFailInfo(dedupResultOpt.get(), logData);
            dispatchFuture = processDispatcher.dispatchFailure(topics.sinkStream(), metadata, failInfo, logData);
        } else {
            dispatchFuture = processDispatcher.dispatchSuccess(topics.sinkStream(), metadata, logData);
        }

        try {
            dispatchFuture.join();
            ack.acknowledge();
        } catch (Exception e) {
            throw (Exception) NestedExceptionUtils.getMostSpecificCause(e);
        }
    }

    // 중복 탐지 조건 및 만료 시간을 포함한 실패 상세 객체 조립
    private FailInfo createFailInfo(DedupResult dedupResult, Map<String, String> logData) {
        ActiveDedupRule rule = dedupResult.rule();
        DedupSpec.Rule spec = dedupResult.matchedSpec();

        String conditionsDetail = spec.conditions().stream()
                .map(c -> c.field() + "=" + logData.get(c.field()))
                .collect(Collectors.joining(", "));

        String detail = String.format(
                "중복 제거됨 - 규칙명: [%s], 위반 조건: [%s], 만료시간: [%d초]",
                rule.name(), conditionsDetail, spec.expirationTime().toTotalSeconds());

        return new FailInfo(ProcessFailCode.DEDUP_DUPLICATED_LOG, rule.dedupRuleId(), rule.name(), detail);
    }
}
