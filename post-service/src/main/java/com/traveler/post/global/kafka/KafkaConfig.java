package com.traveler.post.global.kafka;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
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
        factory.setCommonErrorHandler(commonErrorHandler());
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
    public CommonErrorHandler commonErrorHandler() {
        // Recoverer: 재시도가 모두 실패했거나, 재시도 불가능한 예외 발생 시 호출
        ConsumerRecordRecoverer recoverer = (record, exception) -> {
            kafkaExceptionHandler.handle((Exception) exception, (ConsumerRecord<?, ?>) record);
        };

        // DefaultErrorHandler: (Recoverer, BackOff)
        // FixedBackOff: (대기시간, 최대재시도횟수)
        DefaultErrorHandler handler = new DefaultErrorHandler(recoverer, new FixedBackOff(2000L, 3L));

        handler.addNotRetryableExceptions(
                com.fasterxml.jackson.core.JsonProcessingException.class, // JSON 파싱 에러
                org.springframework.messaging.handler.annotation.support.MethodArgumentTypeMismatchException
                        .class, // 파라미터 타입 불일치
                org.springframework.kafka.listener.ListenerExecutionFailedException.class // 리스너 실행 실패 (내부 원인 확인 필요)
                );
        // 재시도 로깅
        handler.setRetryListeners(kafkaExceptionHandler::logRetry);

        return handler;
    }
}
