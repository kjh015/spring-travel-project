package com.traveler.useractivity.domain.process.format.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.useractivity.domain.process.core.code.ProcessFailCode;
import com.traveler.useractivity.domain.process.core.dispatcher.ProcessDispatcher;
import com.traveler.useractivity.domain.process.core.handler.KafkaAckHandler;
import com.traveler.useractivity.domain.process.core.message.FailInfo;
import com.traveler.useractivity.domain.process.core.message.LogMetadata;
import com.traveler.useractivity.domain.process.core.provider.LogProcessProvider;
import com.traveler.useractivity.domain.process.format.message.RawLog;
import com.traveler.useractivity.domain.process.format.model.ActiveFormatRule;
import com.traveler.useractivity.domain.process.format.provider.FormatRuleProvider;
import com.traveler.useractivity.domain.process.format.service.FormatService;
import com.traveler.useractivity.global.kafka.KafkaTopicProperties;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class FormatProcessor {
    private final ObjectMapper objectMapper;
    private final FormatService formatService;
    private final LogProcessProvider logProcessProvider;
    private final FormatRuleProvider formatRuleProvider;
    private final ProcessDispatcher processDispatcher;
    private final KafkaAckHandler ackHandler;
    private final KafkaTopicProperties topics;

    @KafkaListener(topics = "${app.kafka.topics.format-stream}", groupId = "${app.kafka.groups.format}")
    public void process(
            @Payload String payload,
            @Header(value = "X-Log-Process-Id", required = false) Long logProcessId,
            Acknowledgment ack)
            throws Exception {

        // 필수 헤더 값 검증 및 누락 시 DLT 처리를 위한 예외 발생
        if (logProcessId == null) {
            throw new IllegalArgumentException("Kafka Header 누락: X-Log-Process-Id. Payload: " + payload);
        }

        // JSON 페이로드를 RawLog 객체로 역직렬화
        RawLog rawLog = objectMapper.readValue(payload, RawLog.class);

        // 추적 및 식별을 위한 공통 메타데이터 생성
        LogMetadata metadata = createMetadata(logProcessId);

        // 활성화된 포맷 규칙 조회 및 로그 포맷팅 적용
        List<ActiveFormatRule> activeFormatRules = formatRuleProvider.getActiveFormatRules(logProcessId);
        Map<String, String> formattedLog = formatService.formatLog(rawLog, activeFormatRules);

        // 포맷팅 실패 시
        if (formattedLog == null || formattedLog.isEmpty()) {
            FailInfo failInfo = createFailInfo();
            Map<String, String> failData = Map.of("path", rawLog.path() != null ? rawLog.path() : "");

            // 결과 스트림(Sink)으로 메시지 발행 후 콜백 기반 Ack 처리
            processDispatcher
                    .dispatchFailure(topics.sinkStream(), metadata, failInfo, failData)
                    .whenComplete((result, ex) -> ackHandler.handle(ack, metadata.traceId(), "Failure Sink 전송", ex));
            return;
        }

        // 성공 시 다음 프로세스(Filter) 스트림으로 메시지 발행
        processDispatcher
                .dispatchSuccess(topics.filterStream(), metadata, formattedLog)
                .whenComplete((result, ex) -> ackHandler.handle(ack, metadata.traceId(), "Success Stream 전송", ex));
    }

    // 식별용 메타데이터 조립
    private LogMetadata createMetadata(Long logProcessId) {
        return new LogMetadata(
                UUID.randomUUID().toString(), logProcessId, logProcessProvider.getLogProcessName(logProcessId));
    }

    // 포맷팅 실패 원인 객체 조립
    private FailInfo createFailInfo() {
        return new FailInfo(ProcessFailCode.FORMAT_RULE_NOT_FOUND, null, null, "활성화된 포맷 규칙이 없거나 조건 불일치");
    }
}
