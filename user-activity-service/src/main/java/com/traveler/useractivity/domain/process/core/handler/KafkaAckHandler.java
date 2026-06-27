package com.traveler.useractivity.domain.process.core.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaAckHandler {

    public void handle(Acknowledgment ack, String traceId, String action, Throwable ex) {
        if (ex == null) {
            ack.acknowledge();
        } else {
            log.error(
                    "[Data Loss Prevention] {} 실패. traceId: {}. 해당 메시지는 커밋되지 않으며 재처리 대기 상태로 남습니다.",
                    action,
                    traceId,
                    ex);
        }
    }
}
