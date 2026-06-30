package com.traveler.useractivity.domain.process.sink.processor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.useractivity.domain.process.core.message.LogPayload;
import com.traveler.useractivity.domain.process.sink.service.SinkService;
import java.util.Map;
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

    @KafkaListener(topics = "${app.kafka.topics.sink-stream}", groupId = "${app.kafka.groups.sink}")
    public void process(@Payload String payload, Acknowledgment ack) throws Exception {
        LogPayload<Map<String, String>> logPayload = objectMapper.readValue(payload, new TypeReference<>() {});

        sinkService.sinkLog(logPayload);

        ack.acknowledge();
    }
}
