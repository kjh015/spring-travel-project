package com.traveler.useractivity.domain.process.dedup.processor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.useractivity.domain.process.core.code.ProcessErrorCode;
import com.traveler.useractivity.domain.process.core.dispatcher.ProcessDispatcher;
import com.traveler.useractivity.domain.process.core.message.LogPayload;
import com.traveler.useractivity.domain.process.dedup.service.DedupService;
import com.traveler.useractivity.domain.rule.dedup.entity.DedupRule;
import com.traveler.useractivity.global.kafka.KafkaTopicProperties;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DedupProcessor {
    private final ObjectMapper objectMapper;
    private final DedupService dedupService;
    private final ProcessDispatcher processDispatcher;
    private final KafkaTopicProperties topics;

    public void process(@Payload String payload, Acknowledgment ack) throws Exception {
        LogPayload<Map<String, String>> logPayload = objectMapper.readValue(payload, new TypeReference<>() {});

        String traceId = logPayload.traceId();
        Long logProcessId = logPayload.logProcessId();
        Map<String, String> logData = logPayload.data();

        // 중복된 룰이 있는지 찾는다
        Optional<DedupRule> duplicatedRuleOpt = dedupService.findFirstDuplicatedRule(logData, logProcessId);

        // 중복 탈락
        if (duplicatedRuleOpt.isPresent()) {
            DedupRule duplicatedRule = duplicatedRuleOpt.get();
            String detail = String.format("중복 제거 규칙명: [%s]", duplicatedRule.getName());

            processDispatcher.dispatchFailure(
                    topics.dbStream(),
                    traceId,
                    logProcessId,
                    ProcessErrorCode.DEDUP_DUPLICATED_LOG,
                    duplicatedRule.getId(),
                    detail,
                    logData);

            ack.acknowledge();
            return;
        }

        // 모든 중복 검사 통과 (최종 DB 적재 토픽으로 성공 전송)
        processDispatcher.dispatchSuccess(topics.dbStream(), traceId, logProcessId, logData);
        ack.acknowledge();
    }
}
