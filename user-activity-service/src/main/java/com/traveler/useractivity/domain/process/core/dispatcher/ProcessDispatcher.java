package com.traveler.useractivity.domain.process.core.dispatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.useractivity.domain.process.core.message.FailInfo;
import com.traveler.useractivity.domain.process.core.message.LogMetadata;
import com.traveler.useractivity.domain.process.core.message.LogPayload;
import com.traveler.useractivity.global.kafka.KafkaProducer;
import java.util.concurrent.CompletableFuture;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProcessDispatcher {

    private final ObjectMapper objectMapper;
    private final KafkaProducer kafkaProducer;

    public <T> CompletableFuture<Void> dispatchSuccess(String topic, LogMetadata metadata, T data) throws Exception {
        return send(topic, LogPayload.success(metadata, data));
    }

    public <T> CompletableFuture<Void> dispatchFailure(String topic, LogMetadata metadata, FailInfo failInfo, T data)
            throws Exception {
        return send(topic, LogPayload.failure(metadata, failInfo, data));
    }

    private CompletableFuture<Void> send(String topic, LogPayload<?> payload) throws Exception {
        String jsonString = objectMapper.writeValueAsString(payload);
        return kafkaProducer.send(topic, jsonString).thenAccept(result -> {});
    }
}
