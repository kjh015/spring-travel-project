package com.traveler.useractivity.global.kafka;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.TopicPartition;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaConfig {

    private final KafkaProperties kafkaProperties;
    private final SslBundles sslBundles;
    private final KafkaExceptionHandler kafkaExceptionHandler;

    // Producer
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> nodes = kafkaProperties.buildProducerProperties(sslBundles);
        return new DefaultKafkaProducerFactory<>(nodes);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    // Consumer
    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> nodes = kafkaProperties.buildConsumerProperties(sslBundles);
        return new DefaultKafkaConsumerFactory<>(nodes);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setCommonErrorHandler(commonErrorHandler(kafkaTemplate()));
        Integer concurrency = kafkaProperties.getListener().getConcurrency();
        if (concurrency != null) {
            factory.setConcurrency(concurrency);
        }
        var ackMode = kafkaProperties.getListener().getAckMode();
        if (ackMode != null) {
            factory.getContainerProperties().setAckMode(ackMode);
        }
        return factory;
    }

    @Bean
    public CommonErrorHandler commonErrorHandler(KafkaTemplate<String, String> kafkaTemplate) {
        // DLT 발행기 생성 (원본토픽.DLT 로 전송)
        DefaultErrorHandler handler = getDefaultErrorHandler(kafkaTemplate);

        // ListenerExecutionFailedException 제거
        handler.addNotRetryableExceptions(
                com.fasterxml.jackson.core.JsonProcessingException.class,
                org.springframework.messaging.handler.annotation.support.MethodArgumentTypeMismatchException.class);

        handler.setRetryListeners(kafkaExceptionHandler::logRetry);

        return handler;
    }

    private DefaultErrorHandler getDefaultErrorHandler(KafkaTemplate<String, String> kafkaTemplate) {
        DeadLetterPublishingRecoverer dltRecoverer = new DeadLetterPublishingRecoverer(
                kafkaTemplate, (record, ex) -> new TopicPartition(record.topic() + ".DLT", record.partition()));

        // Recoverer 합성: 커스텀 로깅 후 DLT 격리
        ConsumerRecordRecoverer recoverer = (record, exception) -> {
            kafkaExceptionHandler.handle((Exception) exception, (ConsumerRecord<?, ?>) record);
            dltRecoverer.accept(record, exception);
        };

        return new DefaultErrorHandler(recoverer, new FixedBackOff(2000L, 3L));
    }
}
