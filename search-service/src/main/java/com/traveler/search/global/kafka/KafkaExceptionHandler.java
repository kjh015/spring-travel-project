package com.traveler.search.global.kafka;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.traveler.search.global.exception.SearchServiceException;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaExceptionHandler {
    public void handle(Exception exception, ConsumerRecord<?, ?> record) {
        Throwable cause = NestedExceptionUtils.getMostSpecificCause(exception);

        switch (cause) {
            case SearchServiceException sse -> {
                if (isClientError(sse)) {
                    handleClientError(sse, record);
                } else {
                    handleServerError(sse, record);
                }
            }
            case JsonProcessingException jsonEx -> handleConversionError(jsonEx, record);
            case IOException ioEx -> handleInfraError(ioEx, record);
            case ElasticsearchException esEx -> handleInfraError(esEx, record);
            default -> handleUnknownError(cause, record);
        }
    }

    // 비즈니스 예외 (Client Error - 4xx)
    private void handleClientError(SearchServiceException sse, ConsumerRecord<?, ?> record) {
        log.error(
                "[Final Fail - Client Error] Code: {}, Topic: {}, Msg: {}",
                sse.getCode().getCode(),
                record.topic(),
                sse.getMessage());
    }

    // 비즈니스 예외 (Server Error - 5xx)
    private void handleServerError(SearchServiceException sse, ConsumerRecord<?, ?> record) {
        log.error(
                "[Final Fail - Server Error] Topic: {}, Offset: {}, Msg: {}",
                record.topic(),
                record.offset(),
                sse.getMessage());
    }

    private void handleConversionError(Exception ex, ConsumerRecord<?, ?> record) {
        log.error(
                "[Final Fail - Message Conversion Error] 데이터 형식이 올바르지 않습니다. Topic: {}, Error: {}",
                record.topic(),
                ex.getMessage());
    }

    // 인프라 예외 (IO, ES 관련) - 공통 메서드로 처리
    private void handleInfraError(Exception ex, ConsumerRecord<?, ?> record) {
        log.error(
                "[Final Fail - Infra Error] Target: Elasticsearch, Topic: {}, Error: {}",
                record.topic(),
                ex.getMessage());
    }

    // 알 수 없는 예외
    private void handleUnknownError(Throwable t, ConsumerRecord<?, ?> record) {
        log.error("[Final Fail - Unknown Error] Topic: {}, Error: {}", record.topic(), t.getMessage());
    }

    private boolean isClientError(SearchServiceException sse) {
        int status = sse.getCode().getStatus();
        return status >= 400 && status < 500;
    }

    public void logRetry(ConsumerRecord<?, ?> record, Exception ex, int deliveryAttempt) {
        log.info(
                "Kafka Retry Attempt: {}, Topic: {}, Offset: {}, Error: {}",
                deliveryAttempt,
                record.topic(),
                record.offset(),
                ex.getMessage());
    }
}
