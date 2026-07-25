package com.traveler.useractivity.domain.process.filter.processor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.useractivity.domain.process.core.code.ProcessFailCode;
import com.traveler.useractivity.domain.process.core.dispatcher.ProcessDispatcher;
import com.traveler.useractivity.domain.process.core.executor.KafkaPipelineExecutor;
import com.traveler.useractivity.domain.process.core.logging.ProcessLogContext;
import com.traveler.useractivity.domain.process.core.message.FailInfo;
import com.traveler.useractivity.domain.process.core.message.LogMetadata;
import com.traveler.useractivity.domain.process.core.message.LogPayload;
import com.traveler.useractivity.domain.process.filter.model.ActiveFilterRule;
import com.traveler.useractivity.domain.process.filter.provider.FilterRuleProvider;
import com.traveler.useractivity.domain.process.filter.service.FilterService;
import com.traveler.useractivity.global.kafka.KafkaTopicProperties;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
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
    private final KafkaPipelineExecutor kafkaPipelineExecutor;
    private final KafkaTopicProperties topics;

    @KafkaListener(topics = "${app.kafka.topics.filter-stream}", groupId = "${app.kafka.groups.filter}")
    public CompletableFuture<Void> process(@Payload String payload, Acknowledgment ack) {
        return kafkaPipelineExecutor.execute(ack, () -> {
            LogPayload<Map<String, String>> logPayload = objectMapper.readValue(payload, new TypeReference<>() {});
            LogMetadata metadata = logPayload.metadata();
            Map<String, String> logData = logPayload.data();
            ProcessLogContext.put(metadata, logData);
            log.debug("수신 {}", logData);

            List<ActiveFilterRule> activeFilterRules = filterRuleProvider.getActiveFilterRules(metadata.logProcessId());
            Optional<ActiveFilterRule> failedRuleOpt = filterService.findFirstFailedRule(logData, activeFilterRules);

            if (failedRuleOpt.isPresent()) {
                log.warn("조건 불일치 -> Sink (규칙: {})", failedRuleOpt.get().name());
                FailInfo failInfo = createFailInfo(failedRuleOpt.get());
                return processDispatcher.dispatchFailure(topics.sinkStream(), metadata, failInfo, logData);
            }
            log.info("필터 통과 -> Dedup");
            return processDispatcher.dispatchSuccess(topics.dedupStream(), metadata, logData);
        });
    }

    // 필터 위반 상세 정보를 담은 실패 원인 객체 조립
    private FailInfo createFailInfo(ActiveFilterRule failedRule) {
        String detail = String.format("규칙명: [%s]", failedRule.name());
        return new FailInfo(
                ProcessFailCode.FILTER_CONDITION_MISMATCH, failedRule.filterRuleId(), failedRule.name(), detail);
    }
}
