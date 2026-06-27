package com.traveler.useractivity.domain.process.core.dispatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.useractivity.domain.process.core.code.ProcessErrorCode;
import com.traveler.useractivity.domain.process.core.message.LogPayload;
import com.traveler.useractivity.global.kafka.KafkaProducer;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProcessDispatcher {

    private final ObjectMapper objectMapper;
    private final KafkaProducer kafkaProducer;

    public <T> void dispatchSuccess(String topic, String traceId, Long logProcessId, String logProcessName, T data)
            throws Exception {
        LogPayload<T> payload = LogPayload.success(traceId, logProcessId, logProcessName, data);
        send(topic, payload);
    }

    public <T> void dispatchFailure(
            String topic,
            String traceId,
            Long logProcessId,
            String logProcessName,
            ProcessErrorCode code,
            Long failRuleId,
            String failRuleName,
            String detail,
            T data)
            throws Exception {
        LogPayload<T> payload =
                LogPayload.failure(traceId, logProcessId, logProcessName, code, failRuleId, failRuleName, detail, data);
        send(topic, payload);
    }

    private void send(String topic, LogPayload<?> payload) throws Exception {
        try {
            String jsonString = objectMapper.writeValueAsString(payload);
            kafkaProducer.send(topic, jsonString).get(3, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("메시지 전송 실패: topic=[{}], traceId=[{}]", topic, payload.traceId());
            throw e;
        }
    }
}
