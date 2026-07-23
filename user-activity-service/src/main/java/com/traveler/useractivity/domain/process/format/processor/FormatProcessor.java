package com.traveler.useractivity.domain.process.format.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.useractivity.domain.process.core.code.ProcessFailCode;
import com.traveler.useractivity.domain.process.core.dispatcher.ProcessDispatcher;
import com.traveler.useractivity.domain.process.core.executor.KafkaPipelineExecutor;
import com.traveler.useractivity.domain.process.core.message.FailInfo;
import com.traveler.useractivity.domain.process.core.message.LogMetadata;
import com.traveler.useractivity.domain.process.core.provider.LogProcessProvider;
import com.traveler.useractivity.domain.process.format.message.RawLog;
import com.traveler.useractivity.domain.process.format.model.ActiveFormatRule;
import com.traveler.useractivity.domain.process.format.provider.FormatRuleProvider;
import com.traveler.useractivity.domain.process.format.service.FormatService;
import com.traveler.useractivity.global.kafka.KafkaTopicProperties;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
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
    private final KafkaPipelineExecutor kafkaPipelineExecutor;
    private final KafkaTopicProperties topics;

    @KafkaListener(topics = "${app.kafka.topics.format-stream}", groupId = "${app.kafka.groups.format}")
    public CompletableFuture<Void> process(
            @Payload String payload,
            @Header(value = "X-Log-Process-Id", required = false) byte[] logProcessIdHeader,
            Acknowledgment ack) {

        // fluentd는 헤더를 순수 문자열 바이트로 보내므로(타입 메타데이터 없음) 직접 파싱해야 함
        Long logProcessId = logProcessIdHeader == null
                ? null
                : Long.parseLong(new String(logProcessIdHeader, StandardCharsets.UTF_8).trim());

        return kafkaPipelineExecutor.execute(ack, () -> {
            if (logProcessId == null) {
                throw new IllegalArgumentException(String.format(
                        "Kafka Header 누락: X-Log-Process-Id (Payload Size: %d bytes)",
                        payload != null ? payload.length() : 0));
            }

            RawLog rawLog = objectMapper.readValue(payload, RawLog.class);
            LogMetadata metadata = createMetadata(logProcessId);
            log.info(
                    "[Format] 로그 수신 (traceId: {}, logProcessId: {}, 프로세스명: {})",
                    metadata.traceId(),
                    logProcessId,
                    metadata.logProcessName());

            List<ActiveFormatRule> activeFormatRules = formatRuleProvider.getActiveFormatRules(logProcessId);
            Map<String, String> formattedLog = formatService.formatLog(rawLog, activeFormatRules);

            if (formattedLog == null || formattedLog.isEmpty()) {
                log.warn("[Format] 포맷팅 실패 - Sink로 전달 (traceId: {}, path: {})", metadata.traceId(), rawLog.path());
                FailInfo failInfo = createFailInfo();
                Map<String, String> failData = Map.of("path", rawLog.path() != null ? rawLog.path() : "");
                return processDispatcher.dispatchFailure(topics.sinkStream(), metadata, failInfo, failData);
            }
            log.info("[Format] 포맷팅 성공 - Filter로 전달 (traceId: {})", metadata.traceId());
            return processDispatcher.dispatchSuccess(topics.filterStream(), metadata, formattedLog);
        });
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
