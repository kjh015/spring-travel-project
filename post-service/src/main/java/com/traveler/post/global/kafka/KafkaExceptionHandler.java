package com.traveler.post.global.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.traveler.post.global.exception.PostServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.exception.SdkClientException;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Component
@Slf4j
public class KafkaExceptionHandler {
    public void handle(Exception exception, ConsumerRecord<?, ?> record) {
        Throwable cause = NestedExceptionUtils.getMostSpecificCause(exception);

        switch (cause) {
            case PostServiceException pse -> {
                if (isClientError(pse)) {
                    handleClientError(pse, record);
                } else {
                    handleServerError(pse, record);
                }
            }
            case JsonProcessingException jsonEx -> handleConversionError(jsonEx, record);
            case S3Exception s3Ex -> handleInfraError(s3Ex, record);
            case SdkClientException sdkEx -> handleInfraError(sdkEx, record);
            default -> handleUnknownError(cause, record);
        }
    }

    // 비즈니스 예외 (Client Error - 4xx)
    private void handleClientError(PostServiceException pse, ConsumerRecord<?, ?> record) {
        log.error(
                "[Final Fail - Client Error] Code: {}, Topic: {}, Msg: {}",
                pse.getCode().getCode(),
                record.topic(),
                pse.getMessage());
    }

    // 비즈니스 예외 (Server Error - 5xx)
    private void handleServerError(PostServiceException pse, ConsumerRecord<?, ?> record) {
        log.error(
                "[Final Fail - Server Error] Topic: {}, Offset: {}, Msg: {}",
                record.topic(),
                record.offset(),
                pse.getMessage());
    }

    private void handleConversionError(Exception ex, ConsumerRecord<?, ?> record) {
        log.error(
                "[Final Fail - Message Conversion Error] 데이터 형식이 올바르지 않습니다. Topic: {}, Error: {}",
                record.topic(),
                ex.getMessage());
    }

    private void handleInfraError(Exception ex, ConsumerRecord<?, ?> record) {
        log.error("[Final Fail - Infra Error] Target: AWS S3, Topic: {}, Error: {}", record.topic(), ex.getMessage());
    }

    // 알 수 없는 예외
    private void handleUnknownError(Throwable t, ConsumerRecord<?, ?> record) {
        log.error("[Final Fail - Unknown Error] Topic: {}, Error: {}", record.topic(), t.getMessage());
    }

    private boolean isClientError(PostServiceException pse) {
        int status = pse.getCode().getStatus();
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
