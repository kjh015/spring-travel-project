package com.traveler.useractivity.domain.process.format.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.useractivity.domain.process.format.message.RawLog;
import com.traveler.useractivity.domain.process.format.service.FormatService;
import com.traveler.useractivity.global.kafka.KafkaProducer;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    private final KafkaProducer kafkaProducer;

    @Value("${app.kafka.topics.filter-stream}")
    private String nextTopic;

    @KafkaListener(topics = "${app.kafka.topics.format-stream}", groupId = "${app.kafka.groups.format}")
    public void process(@Payload String payload, @Header("X-Log-Process-Id") Long logProcessId, Acknowledgment ack)
            throws Exception {
        // 역직렬화
        RawLog rawLog = objectMapper.readValue(payload, RawLog.class);

        Map<String, String> formattedLog = formatService.formatLog(rawLog, logProcessId);

        // Drop 처리
        if (formattedLog == null || formattedLog.isEmpty()) {
            log.debug("포맷팅 규칙 불일치. 로그를 폐기합니다.");
            ack.acknowledge();
            return;
        }

        // 직렬화
        String resultPayload = objectMapper.writeValueAsString(formattedLog);

        // 다음 토픽으로 직행 (3초 대기하여 동기적 성공 보장)
        // Header로 logProcessId 보내기 필요
        kafkaProducer.forward(nextTopic, resultPayload).get(3, TimeUnit.SECONDS);

        // 정상 처리 완료 시 수동 커밋
        ack.acknowledge();
    }
}
