package com.traveler.useractivity.domain.process.filter.processor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.useractivity.domain.process.core.code.ProcessFailCode;
import com.traveler.useractivity.domain.process.core.dispatcher.ProcessDispatcher;
import com.traveler.useractivity.domain.process.core.message.LogPayload;
import com.traveler.useractivity.domain.process.filter.model.ActiveFilterRule;
import com.traveler.useractivity.domain.process.filter.provider.FilterRuleProvider;
import com.traveler.useractivity.domain.process.filter.service.FilterService;
import com.traveler.useractivity.global.kafka.KafkaTopicProperties;
import java.util.List;
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
    private final FilterRuleProvider filterRuleProvider;
    private final FilterService filterService;
    private final ProcessDispatcher processDispatcher;
    private final KafkaTopicProperties topics;

    @KafkaListener(topics = "${app.kafka.topics.filter-stream}", groupId = "${app.kafka.groups.filter}")
    public void process(@Payload String payload, Acknowledgment ack) throws Exception {

        LogPayload<Map<String, String>> logPayload = objectMapper.readValue(payload, new TypeReference<>() {});

        String traceId = logPayload.traceId();
        Long logProcessId = logPayload.logProcessId();
        String logProcessName = logPayload.logProcessName();
        Map<String, String> logData = logPayload.data();

        List<ActiveFilterRule> activeFilterRules = filterRuleProvider.getActiveFilterRules(logProcessId);
        // SpEL 필터 검사
        Optional<ActiveFilterRule> failedRuleOpt = filterService.findFirstFailedRule(logData, activeFilterRules);

        // 실패
        if (failedRuleOpt.isPresent()) {
            ActiveFilterRule failedRule = failedRuleOpt.get();
            String detail = String.format("규칙명: [%s]", failedRule.name());

            processDispatcher.dispatchFailure(
                    topics.sinkStream(),
                    traceId,
                    logProcessId,
                    logProcessName,
                    ProcessFailCode.FILTER_CONDITION_MISMATCH,
                    failedRule.filterRuleId(),
                    failedRule.name(),
                    detail,
                    logData);
            ack.acknowledge();
            return;
        }

        // 성공
        processDispatcher.dispatchSuccess(topics.dedupStream(), traceId, logProcessId, logProcessName, logData);
        ack.acknowledge();
    }
}
