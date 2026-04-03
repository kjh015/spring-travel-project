package com.traveler.post.global.kafka;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.converter.RecordMessageConverter;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;

@Configuration
@EnableKafka
@RequiredArgsConstructor
public class KafkaConfig {

    private final KafkaProperties kafkaProperties;

    // Producer
    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> nodes = kafkaProperties.buildProducerProperties();
        return new DefaultKafkaProducerFactory<>(nodes);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    // Consumer
    @Bean
    public RecordMessageConverter converter() {
        return new StringJsonMessageConverter();
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        Map<String, Object> nodes = kafkaProperties.buildConsumerProperties();
        return new DefaultKafkaConsumerFactory<>(nodes);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setRecordMessageConverter(converter());
        factory.setConcurrency(kafkaProperties.getListener().getConcurrency());
        factory.getContainerProperties()
                .setAckMode(kafkaProperties.getListener().getAckMode());
        return factory;
    }
}
