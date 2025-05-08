package com.traveler.logpipeline.kafka.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traveler.logpipeline.kafka.dto.LogDto;
import com.traveler.logpipeline.repository.FormatRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final FormatRepository formatRepository;

    public KafkaConsumerService(FormatRepository formatRepository) {
        this.formatRepository = formatRepository;
    }

    @KafkaListener(topics = "START_TOPIC", groupId = "matomo-log-consumer")
    public void startTopic(ConsumerRecord<String, String> record) {
        try {
            String json = record.value();
            LogDto log = objectMapper.readValue(json, LogDto.class);
            System.out.println("Start Topic Consumed: " + log.toString());
//            Format latest = formatRepository.findTopByOrderByUpdatedTimeDesc();

        } catch (Exception e) {
            System.err.println("Failed to parse JSON: " + e.getMessage());
        }
    }

    @KafkaListener(topics = "FILTER_TOPIC", groupId = "matomo-log-consumer")
    public void filterTopic(ConsumerRecord<String, String> record){
        try {
            String json = record.value();
            System.out.println("Filter Topic Consumed: " + json);
        } catch (Exception e) {
            System.err.println("Failed to parse JSON: " + e.getMessage());
        }
    }

}
