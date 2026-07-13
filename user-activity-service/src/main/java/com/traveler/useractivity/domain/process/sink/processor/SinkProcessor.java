package com.traveler.useractivity.domain.process.sink.processor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.useractivity.domain.process.core.executor.KafkaPipelineExecutor;
import com.traveler.useractivity.domain.process.core.message.LogMetadata;
import com.traveler.useractivity.domain.process.core.message.LogPayload;
import com.traveler.useractivity.domain.process.sink.service.SinkService;
import java.util.Map;
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
public class SinkProcessor {

    private final ObjectMapper objectMapper;
    private final SinkService sinkService;
    private final KafkaPipelineExecutor pipelineExecutor;

    @KafkaListener(topics = "${app.kafka.topics.sink-stream}", groupId = "${app.kafka.groups.sink}")
    public CompletableFuture<Void> process(@Payload String payload, Acknowledgment ack) {
        return pipelineExecutor.execute(ack, () -> {
            LogPayload<Map<String, String>> logPayload = objectMapper.readValue(payload, new TypeReference<>() {});
            LogMetadata metadata = logPayload.metadata();
            if (logPayload.failInfo() != null) {
                log.info(
                        "[Sink] 실패 로그 수신 (traceId: {}, 프로세스명: {}, 실패코드: {})",
                        metadata.traceId(),
                        metadata.logProcessName(),
                        logPayload.failInfo().code());
            } else {
                log.info("[Sink] 정상 로그 수신 (traceId: {}, 프로세스명: {})", metadata.traceId(), metadata.logProcessName());
            }
            return sinkService.sinkLog(logPayload);
        });
    }
}
