package com.traveler.logpipeline.sse.service;

import com.traveler.logpipeline.kafka.dto.LogDto;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class SseService {
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("connected")); //503 에러 방지를 위한 더미 데이터
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return emitter;
    }

    public void sendToClients(LogDto log) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event().name("kafka-message").data(log));
            } catch (IOException e) {
                emitters.remove(emitter);
            }
        }
    }
}

