package com.traveler.useractivity.domain.process.filter.processor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.useractivity.domain.process.core.code.ProcessErrorCode;
import com.traveler.useractivity.domain.process.core.dispatcher.ProcessDispatcher;
import com.traveler.useractivity.domain.process.core.message.LogPayload;
import com.traveler.useractivity.domain.process.filter.service.FilterService;
import com.traveler.useractivity.domain.rule.filter.entity.FilterRule;
import com.traveler.useractivity.global.kafka.KafkaTopicProperties;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FilterProcessor {
    private final ObjectMapper objectMapper;
    private final FilterService filterService;
    private final ProcessDispatcher processDispatcher;
    private final KafkaTopicProperties topics;

    @KafkaListener(topics = "${app.kafka.topics.filter-stream}", groupId = "${app.kafka.groups.filter}")
    public void process(@Payload String payload, Acknowledgment ack) throws Exception {

        LogPayload<Map<String, String>> logPayload = objectMapper.readValue(payload, new TypeReference<>() {});

        String traceId = logPayload.traceId();
        Long logProcessId = logPayload.logProcessId();
        Map<String, String> logData = logPayload.data();

        // SpEL 필터 검사
        Optional<FilterRule> failedRuleOpt = filterService.findFirstFailedRule(logData, logProcessId);

        // 실패
        if (failedRuleOpt.isPresent()) {
            FilterRule failedRule = failedRuleOpt.get();
            String detail = String.format("규칙명: [%s]", failedRule.getName());

            processDispatcher.dispatchFailure(
                    topics.dbStream(),
                    traceId,
                    logProcessId,
                    ProcessErrorCode.FILTER_CONDITION_MISMATCH,
                    failedRule.getId(),
                    detail,
                    logData);

            ack.acknowledge();
            return;
        }

        // 성공
        processDispatcher.dispatchSuccess(topics.dedupStream(), traceId, logProcessId, logData);
        ack.acknowledge();
    }
}
