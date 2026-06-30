package com.traveler.useractivity.domain.process.filter.processor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.useractivity.domain.process.core.code.ProcessFailCode;
import com.traveler.useractivity.domain.process.core.dispatcher.ProcessDispatcher;
import com.traveler.useractivity.domain.process.core.handler.KafkaAckHandler;
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
    private final KafkaAckHandler ackHandler;
    private final KafkaTopicProperties topics;

    @KafkaListener(topics = "${app.kafka.topics.filter-stream}", groupId = "${app.kafka.groups.filter}")
    public void process(@Payload String payload, Acknowledgment ack) throws Exception {

        // JSON 페이로드를 LogPayload 객체로 역직렬화
        LogPayload<Map<String, String>> logPayload = objectMapper.readValue(payload, new TypeReference<>() {});

        // LogPayload에서 메타데이터 및 원본 로그 데이터 추출
        LogMetadata metadata = logPayload.metadata();
        Map<String, String> logData = logPayload.data();

        // 활성화된 필터 규칙 조회 및 위반(Drop) 여부 검사
        List<ActiveFilterRule> activeFilterRules = filterRuleProvider.getActiveFilterRules(metadata.logProcessId());
        Optional<ActiveFilterRule> failedRuleOpt = filterService.findFirstFailedRule(logData, activeFilterRules);

        // 필터 조건 위반 시
        if (failedRuleOpt.isPresent()) {
            FailInfo failInfo = createFailInfo(failedRuleOpt.get());

            // 결과 스트림(Sink)으로 메시지 발행 후 콜백 기반 Ack 처리
            processDispatcher
                    .dispatchFailure(topics.sinkStream(), metadata, failInfo, logData)
                    .whenComplete(
                            (result, ex) -> ackHandler.handle(ack, metadata.traceId(), "Filter 실패 로그 Sink 전송", ex));
            return;
        }

        // 조건 통과 시 다음 프로세스(Dedup) 스트림으로 메시지 발행
        processDispatcher
                .dispatchSuccess(topics.dedupStream(), metadata, logData)
                .whenComplete((result, ex) -> ackHandler.handle(ack, metadata.traceId(), "Filter 성공 로그 Dedup 전송", ex));
    }

    // 필터 위반 상세 정보를 담은 실패 원인 객체 조립
    private FailInfo createFailInfo(ActiveFilterRule failedRule) {
        String detail = String.format("규칙명: [%s]", failedRule.name());
        return new FailInfo(
                ProcessFailCode.FILTER_CONDITION_MISMATCH, failedRule.filterRuleId(), failedRule.name(), detail);
    }
}
