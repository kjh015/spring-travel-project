package com.traveler.useractivity.domain.process.format.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.useractivity.domain.process.core.code.ProcessErrorCode;
import com.traveler.useractivity.domain.process.core.dispatcher.ProcessDispatcher;
import com.traveler.useractivity.domain.process.format.message.RawLog;
import com.traveler.useractivity.domain.process.format.service.FormatService;
import com.traveler.useractivity.global.kafka.KafkaTopicProperties;
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
    private final ProcessDispatcher processDispatcher;
    private final KafkaTopicProperties topics;

    @KafkaListener(topics = "${app.kafka.topics.format-stream}", groupId = "${app.kafka.groups.format}")
    public void process(@Payload String payload, @Header("X-Log-Process-Id") Long logProcessId, Acknowledgment ack)
            throws Exception {
        if (logProcessId == null) {
            log.warn("Kafka Header에 logProcessId가 없습니다. 로그를 폐기합니다. Payload: {}", payload);
            ack.acknowledge();
            return;
        }

        // 고유 추적 ID(traceId) 발급
        String traceId = UUID.randomUUID().toString();

        // 역직렬화
        RawLog rawLog = objectMapper.readValue(payload, RawLog.class);

        Map<String, String> formattedLog = formatService.formatLog(rawLog, logProcessId);

        // 실패
        if (formattedLog == null || formattedLog.isEmpty()) {
            Map<String, String> failData = Map.of("path", rawLog.path() != null ? rawLog.path() : "");

            processDispatcher.dispatchFailure(
                    topics.sinkStream(),
                    traceId,
                    logProcessId,
                    ProcessErrorCode.FORMAT_RULE_NOT_FOUND,
                    null,
                    "활성화된 포맷 규칙이 없거나 조건 불일치",
                    failData);
            log.debug("포맷팅 실패 로그를 DB 토픽으로 전송했습니다. traceId: {}", traceId);

            ack.acknowledge();
            return;
        }

        // 성공
        processDispatcher.dispatchSuccess(topics.filterStream(), traceId, logProcessId, formattedLog);
        ack.acknowledge();
    }
}
