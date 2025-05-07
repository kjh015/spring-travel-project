package com.traveler.logpipeline.kafka.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.logpipeline.kafka.dto.LogDto;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Service;

@Service
public class LogConsumerService {
    private final ObjectMapper objectMapper = new ObjectMapper();


    //@KafkaListener(topics = "START_TOPIC", groupId = "matomo-log-consumer")
    public void consume(ConsumerRecord<String, String> record) {
        try {
            String json = record.value();
            LogDto log = objectMapper.readValue(json, LogDto.class);
            System.out.println("Consumed: " + log.toString());
        } catch (Exception e) {
            System.err.println("Failed to parse JSON: " + e.getMessage());
        }
    }
}
